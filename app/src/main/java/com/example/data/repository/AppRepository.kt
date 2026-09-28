package com.example.data.repository

import com.example.data.local.KabadiwalaDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppRepository(private val dao: KabadiwalaDao) {

    // Current Session State
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    private val _currentCollectorProfile = MutableStateFlow<CollectorProfileEntity?>(null)
    val currentCollectorProfile: StateFlow<CollectorProfileEntity?> = _currentCollectorProfile.asStateFlow()

    private val _currentRecyclerProfile = MutableStateFlow<RecyclerProfileEntity?>(null)
    val currentRecyclerProfile: StateFlow<RecyclerProfileEntity?> = _currentRecyclerProfile.asStateFlow()

    // Offline / Online Mode Toggle
    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    // Sync notification message banner
    private val _syncMessage = MutableStateFlow<String?>(null)
    val syncMessage: StateFlow<String?> = _syncMessage.asStateFlow()

    fun setOnlineMode(online: Boolean) {
        _isOnline.value = online
    }

    fun clearSyncMessage() {
        _syncMessage.value = null
    }

    // --- Authentication ---
    suspend fun login(identifier: String, password: String):Result<UserEntity> {
        val user = dao.findUserByIdentifier(identifier.trim())
            ?: return Result.failure(Exception("No account found with this username or phone number"))

        // Simple demo hash check
        if (user.passwordHash != password.trim() && user.passwordHash != "hash_${password.trim()}") {
            return Result.failure(Exception("Incorrect password. Please try again."))
        }

        _currentUser.value = user
        loadUserProfile(user)
        return Result.success(user)
    }

    suspend fun quickLoginAs(role: UserRole): Result<UserEntity> {
        val user = when (role) {
            UserRole.COLLECTOR -> dao.findUserByIdentifier("collector")
            UserRole.RECYCLER -> dao.findUserByIdentifier("recycler")
            UserRole.ADMIN -> dao.findUserByIdentifier("admin")
        } ?: return Result.failure(Exception("User not found"))

        _currentUser.value = user
        loadUserProfile(user)
        return Result.success(user)
    }

    private suspend fun loadUserProfile(user: UserEntity) {
        when (user.role) {
            UserRole.COLLECTOR.name -> {
                _currentCollectorProfile.value = dao.getCollectorProfile(user.id)
            }
            UserRole.RECYCLER.name -> {
                _currentRecyclerProfile.value = dao.getRecyclerProfile(user.id)
            }
            else -> {}
        }
    }

    fun logout() {
        _currentUser.value = null
        _currentCollectorProfile.value = null
        _currentRecyclerProfile.value = null
    }

    suspend fun resetPassword(identifier: String, newPassword: String): Result<String> {
        val user = dao.findUserByIdentifier(identifier.trim())
            ?: return Result.failure(Exception("No account found with username or phone: $identifier"))
        dao.updateUserPassword(user.id, newPassword.trim())
        dao.insertAuditLog(
            AuditLogEntity(
                actorUserId = user.id,
                actorRole = user.role,
                action = "PASSWORD_RESET",
                details = "Password updated for user ${user.username}"
            )
        )
        return Result.success("Password successfully updated. You can now login.")
    }

    // --- Registration ---
    suspend fun registerCollector(
        user: UserEntity,
        profile: CollectorProfileEntity
    ): Result<Unit> {
        dao.insertUser(user)
        dao.insertCollectorProfile(profile)
        dao.insertAuditLog(
            AuditLogEntity(
                actorUserId = user.id,
                actorRole = "COLLECTOR",
                action = "COLLECTOR_REGISTRATION",
                details = "Registered collector: ${profile.fullName}, phone: ${profile.phone}"
            )
        )
        dao.insertNotification(
            NotificationEntity(
                recipientUserId = "ADMIN",
                role = "ADMIN",
                title = "New Collector Registration",
                message = "${profile.fullName} registered from ${profile.city}. Pending review."
            )
        )
        return Result.success(Unit)
    }

    suspend fun registerRecycler(
        user: UserEntity,
        profile: RecyclerProfileEntity
    ): Result<Unit> {
        dao.insertUser(user)
        dao.insertRecyclerProfile(profile)
        dao.insertAuditLog(
            AuditLogEntity(
                actorUserId = user.id,
                actorRole = "RECYCLER",
                action = "RECYCLER_REGISTRATION",
                details = "Registered recycler: ${profile.orgName}, cert: ${profile.certNumber}"
            )
        )
        dao.insertNotification(
            NotificationEntity(
                recipientUserId = "ADMIN",
                role = "ADMIN",
                title = "New Recycler Authorization Request",
                message = "${profile.orgName} submitted certificate #${profile.certNumber} for verification."
            )
        )
        return Result.success(Unit)
    }

    // --- Material Lot Creation & Sync ---
    suspend fun createMaterialLot(lot: MaterialLotEntity): Result<String> {
        val isCurrentlyOnline = _isOnline.value
        val lotToSave = lot.copy(isSynced = isCurrentlyOnline)
        dao.insertMaterialLot(lotToSave)

        if (!isCurrentlyOnline) {
            // Queue for offline sync
            dao.insertSyncQueueItem(
                SyncQueueEntity(
                    actionType = "CREATE_LOT",
                    payloadJson = lot.lotId,
                    status = "PENDING"
                )
            )
        }

        dao.insertAuditLog(
            AuditLogEntity(
                actorUserId = lot.collectorId,
                actorRole = "COLLECTOR",
                action = "CREATE_LOT",
                details = "Created lot ${lot.lotId} for ${lot.category} (${lot.weightKg} kg)"
            )
        )
        return Result.success(lot.lotId)
    }

    suspend fun updateMaterialLot(lot: MaterialLotEntity) {
        dao.updateMaterialLot(lot)
    }

    suspend fun getLotById(lotId: String): MaterialLotEntity? = dao.getLotById(lotId)

    // --- Recycler Matching & Requests ---
    suspend fun getApprovedRecyclers(): List<RecyclerProfileEntity> = dao.getApprovedRecyclers()

    suspend fun createPickupRequest(request: PickupRequestEntity): Long {
        val id = dao.insertPickupRequest(request)
        // Notify recycler
        dao.insertNotification(
            NotificationEntity(
                recipientUserId = request.recyclerId,
                role = "RECYCLER",
                title = "New E-Waste Lot Request",
                message = "Pickup request from ${request.collectorName}: ${request.materialCategory} (${request.weightKg} kg)"
            )
        )
        // Also update lot status
        val lot = dao.getLotById(request.lotId)
        if (lot != null) {
            dao.updateMaterialLot(
                lot.copy(
                    matchedRecyclerId = request.recyclerId,
                    matchedRecyclerName = request.recyclerName,
                    status = LotStatus.PICKUP_REQUESTED.name
                )
            )
        }
        return id
    }

    suspend fun updatePickupStatus(requestId: Long, newStatus: PickupStatus) {
        dao.updatePickupStatus(requestId, newStatus.name)
        val request = dao.getPickupRequestById(requestId)
        if (request != null) {
            // Notify collector
            val statusLabel = when (newStatus) {
                PickupStatus.ACCEPTED -> "Recycler ${request.recyclerName} accepted your pickup request!"
                PickupStatus.PICKUP_SCHEDULED -> "Pickup scheduled with ${request.recyclerName}."
                PickupStatus.PICKED_UP -> "E-Waste picked up by ${request.recyclerName}."
                PickupStatus.HANDOVER_CONFIRMED -> "Handover confirmed! Preparing payment."
                PickupStatus.COMPLETED -> "Transaction completed successfully!"
                PickupStatus.REJECTED -> "Pickup request declined by ${request.recyclerName}."
                else -> "Status update: ${newStatus.name}"
            }
            dao.insertNotification(
                NotificationEntity(
                    recipientUserId = request.collectorId,
                    role = "COLLECTOR",
                    title = "Pickup Status Update",
                    message = statusLabel
                )
            )
        }
    }

    // --- Digital Handover & Transaction ---
    suspend fun recordHandoverAndCompleteTransaction(
        handover: HandoverRecordEntity,
        pricePerKg: Double,
        collectorName: String,
        recyclerName: String,
        category: String,
        location: String
    ): String {
        dao.insertHandover(handover)

        // Generate Transaction
        val txnId = "TXN-${SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())}-${(1000..9999).random()}"
        val totalAmount = handover.actualWeightKg * pricePerKg

        val txn = TransactionRecordEntity(
            transactionId = txnId,
            lotId = handover.lotId,
            handoverRefId = handover.referenceId,
            collectorId = handover.collectorId,
            collectorName = collectorName,
            recyclerId = handover.recyclerId,
            recyclerName = recyclerName,
            category = category,
            weightKg = handover.actualWeightKg,
            pricePerKg = pricePerKg,
            totalAmount = totalAmount,
            dateTimestamp = System.currentTimeMillis(),
            location = location,
            handoverStatus = "CONFIRMED",
            paymentStatus = PaymentStatus.RECORDED.name,
            notes = "Digital Handover ref: ${handover.referenceId}"
        )
        dao.insertTransaction(txn)

        // Update pickup status to completed
        dao.updatePickupStatus(handover.pickupRequestId, PickupStatus.COMPLETED.name)

        // Update lot status
        val lot = dao.getLotById(handover.lotId)
        if (lot != null) {
            dao.updateMaterialLot(lot.copy(status = LotStatus.COMPLETED.name, weightKg = handover.actualWeightKg))
        }

        // Notify both parties
        dao.insertNotification(
            NotificationEntity(
                recipientUserId = handover.collectorId,
                role = "COLLECTOR",
                title = "Payment Recorded: ₹${totalAmount.toInt()}",
                message = "Handover confirmed for $category (${handover.actualWeightKg} kg). Ref: ${handover.referenceId}"
            )
        )

        dao.insertNotification(
            NotificationEntity(
                recipientUserId = handover.recyclerId,
                role = "RECYCLER",
                title = "Material Received",
                message = "Received $category (${handover.actualWeightKg} kg) from $collectorName. Txn: $txnId"
            )
        )

        return txnId
    }

    // --- Synchronization ---
    suspend fun synchronizeOfflineQueue(): Int {
        val unsyncedLots = dao.getUnsyncedLots()
        var count = 0
        for (lot in unsyncedLots) {
            dao.markLotSynced(lot.lotId)
            count++
        }
        val pendingQueue = dao.getPendingSyncItems()
        for (item in pendingQueue) {
            dao.markSyncQueueItemSynced(item.id)
            count++
        }
        dao.clearSyncedQueue()
        _syncMessage.value = "✓ $count records synchronized successfully"
        return count
    }

    // --- Admin Operations ---
    suspend fun updateCollectorApproval(userId: String, isApproved: Boolean) {
        val status = if (isApproved) UserStatus.APPROVED.name else UserStatus.REJECTED.name
        dao.updateUserStatus(userId, status)
        dao.insertNotification(
            NotificationEntity(
                recipientUserId = userId,
                role = "COLLECTOR",
                title = if (isApproved) "Registration Approved! 🎉" else "Registration Application Update",
                message = if (isApproved)
                    "Welcome to Kabadiwala Connect! Your collector account is now active."
                else
                    "Your registration was not approved. Please verify your submitted documents."
            )
        )
    }

    suspend fun updateRecyclerAuthorization(userId: String, isApproved: Boolean) {
        val status = if (isApproved) RecyclerCertStatus.APPROVED.name else RecyclerCertStatus.REJECTED.name
        dao.updateUserStatus(userId, if (isApproved) UserStatus.APPROVED.name else UserStatus.REJECTED.name)
        dao.updateRecyclerCertStatus(userId, status)
        dao.insertNotification(
            NotificationEntity(
                recipientUserId = userId,
                role = "RECYCLER",
                title = if (isApproved) "Certificate Verified & Approved! 🛡️" else "Certificate Verification Update",
                message = if (isApproved)
                    "Your CPCB/SPCB recycling certificate is verified. You can now accept e-waste lots."
                else
                    "Your certificate could not be verified. Please upload an updated CPCB/SPCB authorization."
            )
        )
    }

    suspend fun updateTransactionPayment(txnId: String, status: PaymentStatus, notes: String) {
        dao.updateTransactionPayment(txnId, status.name, notes)
    }

    suspend fun updateRecyclerSettings(
        userId: String,
        ratesJson: String,
        categories: String,
        minQty: Double,
        pickupAvail: Boolean,
        radius: Double
    ) {
        dao.updateRecyclerSettings(userId, ratesJson, categories, minQty, pickupAvail, radius)
        _currentRecyclerProfile.value = dao.getRecyclerProfile(userId)
    }

    // --- Flows ---
    fun getAllUsersFlow(): Flow<List<UserEntity>> = dao.getAllUsersFlow()
    fun getAllLotsFlow(): Flow<List<MaterialLotEntity>> = dao.getAllLotsFlow()
    fun getLotsByCollectorFlow(collectorId: String): Flow<List<MaterialLotEntity>> = dao.getLotsByCollectorFlow(collectorId)
    fun getPickupRequestsForCollectorFlow(collectorId: String): Flow<List<PickupRequestEntity>> = dao.getPickupRequestsForCollectorFlow(collectorId)
    fun getPickupRequestsForRecyclerFlow(recyclerId: String): Flow<List<PickupRequestEntity>> = dao.getPickupRequestsForRecyclerFlow(recyclerId)
    fun getAllPickupRequestsFlow(): Flow<List<PickupRequestEntity>> = dao.getAllPickupRequestsFlow()
    fun getTransactionsForCollectorFlow(collectorId: String): Flow<List<TransactionRecordEntity>> = dao.getTransactionsForCollectorFlow(collectorId)
    fun getTransactionsForRecyclerFlow(recyclerId: String): Flow<List<TransactionRecordEntity>> = dao.getTransactionsForRecyclerFlow(recyclerId)
    fun getAllTransactionsFlow(): Flow<List<TransactionRecordEntity>> = dao.getAllTransactionsFlow()
    fun getNotificationsFlow(userId: String, role: String): Flow<List<NotificationEntity>> = dao.getNotificationsFlow(userId, role)
    fun getAuditLogsFlow(): Flow<List<AuditLogEntity>> = dao.getAuditLogsFlow()
    fun getPendingSyncCountFlow(): Flow<Int> = dao.getPendingSyncCountFlow()
    fun getAllCollectorProfilesFlow(): Flow<List<CollectorProfileEntity>> = dao.getAllCollectorProfilesFlow()
    fun getAllRecyclerProfilesFlow(): Flow<List<RecyclerProfileEntity>> = dao.getAllRecyclerProfilesFlow()

    // --- Seed Demo Data ---
    suspend fun seedInitialDataIfNeeded() {
        val existing = dao.findUserByIdentifier("collector")
        if (existing != null) return // Already seeded

        // 1. Admin
        dao.insertUser(
            UserEntity(
                id = "admin_01",
                username = "admin",
                phone = "+91 99999 88888",
                passwordHash = "admin123",
                role = UserRole.ADMIN.name,
                status = UserStatus.APPROVED.name
            )
        )

        // 2. Demo Collector (Approved)
        val collectorId = "collector_demo"
        dao.insertUser(
            UserEntity(
                id = collectorId,
                username = "collector",
                phone = "+91 98765 43210",
                passwordHash = "password123",
                role = UserRole.COLLECTOR.name,
                status = UserStatus.APPROVED.name
            )
        )
        dao.insertCollectorProfile(
            CollectorProfileEntity(
                userId = collectorId,
                fullName = "Ramu E-Waste Mitra",
                phone = "+91 98765 43210",
                address = "Plot 42, Dharavi Main Road",
                city = "Mumbai",
                state = "Maharashtra",
                pincode = "400017",
                gpsLat = 19.0434,
                gpsLng = 72.8562,
                govtIdNumber = "AADHAAR-8921-3482-1920",
                preferredArea = "Central Mumbai & Suburbs",
                languagesKnown = "Hindi, Marathi, English"
            )
        )

        // 3. Pending Collector (For testing Admin approvals)
        val pendingCollectorId = "collector_pending_01"
        dao.insertUser(
            UserEntity(
                id = pendingCollectorId,
                username = "rajesh_kumar",
                phone = "+91 91234 56789",
                passwordHash = "password123",
                role = UserRole.COLLECTOR.name,
                status = UserStatus.PENDING.name
            )
        )
        dao.insertCollectorProfile(
            CollectorProfileEntity(
                userId = pendingCollectorId,
                fullName = "Rajesh Kumar",
                phone = "+91 91234 56789",
                address = "Shop 12, Seelampur Market",
                city = "New Delhi",
                state = "Delhi",
                pincode = "110053",
                gpsLat = 28.6692,
                gpsLng = 77.2694,
                govtIdNumber = "VOTER-ID-DL-98213",
                preferredArea = "East Delhi & Shahdara",
                languagesKnown = "Hindi, English"
            )
        )

        // 4. Authorized Recycler 1 (Approved)
        val recycler1Id = "recycler_greentech"
        dao.insertUser(
            UserEntity(
                id = recycler1Id,
                username = "recycler",
                phone = "+91 98200 12345",
                passwordHash = "password123",
                role = UserRole.RECYCLER.name,
                status = UserStatus.APPROVED.name
            )
        )
        val ratesJson1 = "{\"Laptops\": 290.0, \"Mobile Phones\": 360.0, \"Circuit Boards\": 440.0, \"Batteries\": 150.0, \"Cables\": 200.0, \"Computers\": 230.0, \"Televisions\": 90.0}"
        dao.insertRecyclerProfile(
            RecyclerProfileEntity(
                userId = recycler1Id,
                orgName = "GreenTech Recycling Pvt Ltd",
                authorizedPerson = "Anil Verma (Managing Director)",
                phone = "+91 98200 12345",
                address = "MIDC Industrial Area, Phase II",
                city = "Navi Mumbai",
                state = "Maharashtra",
                pincode = "400705",
                gpsLat = 19.0330,
                gpsLng = 73.0297,
                certNumber = "CPCB/EW-REG/MH/2024/0912",
                certAuthority = "Maharashtra Pollution Control Board (MPCB)",
                certIssueDate = "2024-01-15",
                certExpiryDate = "2027-01-14",
                certDocumentUri = "https://example.com/certs/greentech_cpcb.pdf",
                certStatus = RecyclerCertStatus.APPROVED.name,
                acceptedCategoriesCsv = "Laptops, Mobile Phones, Circuit Boards, Batteries, Cables, Computers, Televisions",
                processingCapacityKgPerDay = 5000.0,
                minQuantityKg = 5.0,
                pickupRadiusKm = 40.0,
                pickupAvailable = true,
                ratesJson = ratesJson1
            )
        )

        // 5. Authorized Recycler 2 (EcoClean Solutions)
        val recycler2Id = "recycler_ecoclean"
        dao.insertUser(
            UserEntity(
                id = recycler2Id,
                username = "ecoclean",
                phone = "+91 98300 54321",
                passwordHash = "password123",
                role = UserRole.RECYCLER.name,
                status = UserStatus.APPROVED.name
            )
        )
        val ratesJson2 = "{\"Laptops\": 275.0, \"Mobile Phones\": 340.0, \"Circuit Boards\": 410.0, \"Batteries\": 135.0, \"Cables\": 185.0, \"Printers\": 70.0}"
        dao.insertRecyclerProfile(
            RecyclerProfileEntity(
                userId = recycler2Id,
                orgName = "EcoClean Recovery Works",
                authorizedPerson = "Sumanth Rao",
                phone = "+91 98300 54321",
                address = "TTC Industrial Zone, Pawane",
                city = "Thane",
                state = "Maharashtra",
                pincode = "400703",
                gpsLat = 19.1663,
                gpsLng = 72.9985,
                certNumber = "SPCB/EW/THN/2023/4412",
                certAuthority = "State Pollution Control Board",
                certIssueDate = "2023-06-10",
                certExpiryDate = "2026-06-09",
                certDocumentUri = "https://example.com/certs/ecoclean.pdf",
                certStatus = RecyclerCertStatus.APPROVED.name,
                acceptedCategoriesCsv = "Laptops, Mobile Phones, Circuit Boards, Batteries, Cables, Printers",
                processingCapacityKgPerDay = 3000.0,
                minQuantityKg = 10.0,
                pickupRadiusKm = 25.0,
                pickupAvailable = true,
                ratesJson = ratesJson2
            )
        )

        // 6. Recycler Pending Verification (For testing admin authorization approvals)
        val recyclerPendingId = "recycler_pending_01"
        dao.insertUser(
            UserEntity(
                id = recyclerPendingId,
                username = "bharat_recycle",
                phone = "+91 97111 22334",
                passwordHash = "password123",
                role = UserRole.RECYCLER.name,
                status = UserStatus.PENDING.name
            )
        )
        dao.insertRecyclerProfile(
            RecyclerProfileEntity(
                userId = recyclerPendingId,
                orgName = "Bharat Circular Resources",
                authorizedPerson = "Deepak Patel",
                phone = "+91 97111 22334",
                address = "GIDC Industrial Estate, Vatva",
                city = "Ahmedabad",
                state = "Gujarat",
                pincode = "382440",
                gpsLat = 22.9557,
                gpsLng = 72.6366,
                certNumber = "GPCB/EWASTE/APP/2026/001",
                certAuthority = "Gujarat Pollution Control Board",
                certIssueDate = "2026-02-01",
                certExpiryDate = "2029-01-31",
                certDocumentUri = "https://example.com/certs/bharat.pdf",
                certStatus = RecyclerCertStatus.PENDING_VERIFICATION.name,
                acceptedCategoriesCsv = "Laptops, Circuit Boards, Cables, Batteries",
                processingCapacityKgPerDay = 4000.0,
                minQuantityKg = 15.0,
                pickupRadiusKm = 50.0,
                pickupAvailable = true,
                ratesJson = "{\"Laptops\": 285.0, \"Circuit Boards\": 430.0, \"Cables\": 195.0}"
            )
        )

        // 7. Seed Sample Material Lots for Demo Collector
        val lot1 = MaterialLotEntity(
            lotId = "LOT-2026-001",
            collectorId = collectorId,
            dateTime = System.currentTimeMillis() - 86400000L * 3,
            description = "Dell & Lenovo office laptops with power supplies",
            weightKg = 18.5,
            locationName = "Bandra West, Mumbai",
            gpsLat = 19.0596,
            gpsLng = 72.8295,
            category = "Laptops",
            subCategory = "Laptop Computer / Notebook",
            aiConfidence = 95,
            condition = MaterialCondition.PARTIALLY_WORKING.name,
            estimatedPricePerKg = 290.0,
            estimatedTotalValue = 5365.0,
            matchedRecyclerId = recycler1Id,
            matchedRecyclerName = "GreenTech Recycling Pvt Ltd",
            status = LotStatus.COMPLETED.name,
            isSynced = true
        )
        dao.insertMaterialLot(lot1)

        val lot2 = MaterialLotEntity(
            lotId = "LOT-2026-002",
            collectorId = collectorId,
            dateTime = System.currentTimeMillis() - 86400000L,
            description = "Telecom server motherboards and telecom cards",
            weightKg = 12.0,
            locationName = "Andheri East, Mumbai",
            gpsLat = 19.1136,
            gpsLng = 72.8697,
            category = "Circuit Boards",
            subCategory = "Printed Circuit Boards (PCB)",
            aiConfidence = 96,
            condition = MaterialCondition.WORKING.name,
            estimatedPricePerKg = 440.0,
            estimatedTotalValue = 5280.0,
            matchedRecyclerId = recycler1Id,
            matchedRecyclerName = "GreenTech Recycling Pvt Ltd",
            status = LotStatus.PICKUP_REQUESTED.name,
            isSynced = true
        )
        dao.insertMaterialLot(lot2)

        // 8. Seed Sample Pickup Requests
        val pickup1 = PickupRequestEntity(
            lotId = lot1.lotId,
            collectorId = collectorId,
            collectorName = "Ramu E-Waste Mitra",
            collectorPhone = "+91 98765 43210",
            recyclerId = recycler1Id,
            recyclerName = "GreenTech Recycling Pvt Ltd",
            materialCategory = "Laptops",
            weightKg = 18.5,
            agreedPricePerKg = 290.0,
            totalValue = 5365.0,
            pickupAddress = "Plot 42, Dharavi Main Road, Mumbai",
            gpsLat = 19.0434,
            gpsLng = 72.8562,
            preferredDateTime = "Yesterday, 2:00 PM",
            status = PickupStatus.COMPLETED.name
        )
        dao.insertPickupRequest(pickup1)

        val pickup2 = PickupRequestEntity(
            lotId = lot2.lotId,
            collectorId = collectorId,
            collectorName = "Ramu E-Waste Mitra",
            collectorPhone = "+91 98765 43210",
            recyclerId = recycler1Id,
            recyclerName = "GreenTech Recycling Pvt Ltd",
            materialCategory = "Circuit Boards",
            weightKg = 12.0,
            agreedPricePerKg = 440.0,
            totalValue = 5280.0,
            pickupAddress = "Shop 5, Andheri East, Mumbai",
            gpsLat = 19.1136,
            gpsLng = 72.8697,
            preferredDateTime = "Today, 4:30 PM",
            status = PickupStatus.ACCEPTED.name
        )
        dao.insertPickupRequest(pickup2)

        // 9. Seed Completed Handover & Transaction
        val handoverRef = "KBC-2026-000124"
        dao.insertHandover(
            HandoverRecordEntity(
                referenceId = handoverRef,
                lotId = lot1.lotId,
                pickupRequestId = 1L,
                collectorId = collectorId,
                recyclerId = recycler1Id,
                actualWeightKg = 18.5,
                photoUri = "",
                gpsLat = 19.0434,
                gpsLng = 72.8562,
                timestamp = System.currentTimeMillis() - 86400000L * 2,
                confirmedByCollector = true,
                confirmedByRecycler = true
            )
        )

        dao.insertTransaction(
            TransactionRecordEntity(
                transactionId = "TXN-2026-1042",
                lotId = lot1.lotId,
                handoverRefId = handoverRef,
                collectorId = collectorId,
                collectorName = "Ramu E-Waste Mitra",
                recyclerId = recycler1Id,
                recyclerName = "GreenTech Recycling Pvt Ltd",
                category = "Laptops",
                weightKg = 18.5,
                pricePerKg = 290.0,
                totalAmount = 5365.0,
                dateTimestamp = System.currentTimeMillis() - 86400000L * 2,
                location = "Dharavi, Mumbai",
                handoverStatus = "CONFIRMED",
                paymentStatus = PaymentStatus.PAID.name,
                notes = "Direct NEFT Transfer confirmed."
            )
        )

        // 10. Notifications
        dao.insertNotification(
            NotificationEntity(
                recipientUserId = collectorId,
                role = "COLLECTOR",
                title = "Payment Received: ₹5,365",
                message = "GreenTech Recycling transferred ₹5,365 for your Laptop lot (KBC-2026-000124)."
            )
        )
        dao.insertNotification(
            NotificationEntity(
                recipientUserId = collectorId,
                role = "COLLECTOR",
                title = "Pickup Accepted",
                message = "GreenTech Recycling accepted your Circuit Boards pickup request (12 kg)."
            )
        )
    }
}
