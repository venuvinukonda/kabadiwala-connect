package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.KabadiwalaDatabase
import com.example.data.model.*
import com.example.data.repository.AppRepository
import com.example.service.ai.*
import com.example.service.audio.AudioHelpManager
import com.example.service.localization.AppLanguage
import com.example.service.localization.LocalizationManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LotCreationState(
    val step: Int = 1, // 1: Details/Weight/Photo, 2: AI Classification, 3: Condition, 4: Price Estimation, 5: Recycler Match, 6: Success
    val photoUri: String = "",
    val weightInput: String = "15.0",
    val description: String = "",
    val location: String = "Dharavi Junction, Mumbai",
    val lat: Double = 19.0434,
    val lng: Double = 72.8562,
    // AI Classification
    val aiClassification: MaterialClassificationResult? = null,
    val selectedCategory: String = "Laptops",
    val selectedSubcategory: String = "Laptop Computer",
    val isAiConfirmed: Boolean = false,
    val isCustomCategoryDialogShowing: Boolean = false,
    // Condition
    val selectedCondition: MaterialCondition = MaterialCondition.PARTIALLY_WORKING,
    // ML Price Estimation
    val priceEstimate: PriceEstimateResult? = null,
    val isEstimatingPrice: Boolean = false,
    // Recycler Matching
    val matchingRecyclers: List<RecyclerMatchResult> = emptyList(),
    val selectedRecyclerMatch: RecyclerMatchResult? = null,
    val isMatching: Boolean = false,
    val generatedLotId: String = ""
)

data class RouteOptimizationResult(
    val stops: List<PickupRequestEntity> = emptyList(),
    val polylinePoints: List<Pair<Double, Double>> = emptyList(),
    val totalDistanceKm: Double = 0.0,
    val estimatedDurationMinutes: Int = 0,
    val totalWeightKg: Double = 0.0,
    val totalEstimatedValue: Double = 0.0
)

@OptIn(ExperimentalCoroutinesApi::class)
class KabadiwalaViewModel(application: Application) : AndroidViewModel(application) {

    private val database = KabadiwalaDatabase.getDatabase(application)
    val repository = AppRepository(database.kabadiwalaDao())
    val audioManager = AudioHelpManager(application)

    // Modular AI/ML Services
    private val classificationService: MaterialClassificationService = MockMaterialClassificationService()
    private val priceEstimationService: PriceEstimationService = MockPriceEstimationService()
    private val recyclerMatchingService: RecyclerMatchingService = MockRecyclerMatchingService()

    // Auth & Session
    val currentUser = repository.currentUser
    val currentCollectorProfile = repository.currentCollectorProfile
    val currentRecyclerProfile = repository.currentRecyclerProfile
    val isOnline = repository.isOnline
    val syncMessage = repository.syncMessage

    // Multilingual & Audio
    val currentLanguage = LocalizationManager.currentLanguage
    val isSpeaking = audioManager.isSpeaking
    val isPaused = audioManager.isPaused
    val speechRate = audioManager.speechRate
    val currentSpokenText = audioManager.currentSpokenText
    val voiceUnavailableMessage = audioManager.voiceUnavailableMessage

    // Lists
    val allLots = repository.getAllLotsFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allPickups = repository.getAllPickupRequestsFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allTransactions = repository.getAllTransactionsFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allUsers = repository.getAllUsersFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val collectorProfiles = repository.getAllCollectorProfilesFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val recyclerProfiles = repository.getAllRecyclerProfilesFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val auditLogs = repository.getAuditLogsFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val pendingSyncCount = repository.getPendingSyncCountFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Current collector / recycler filtered lists
    val collectorLots: StateFlow<List<MaterialLotEntity>> = combine(allLots, currentUser) { lots, user ->
        if (user != null && user.role == UserRole.COLLECTOR.name) {
            lots.filter { it.collectorId == user.id }
        } else emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val collectorPickups: StateFlow<List<PickupRequestEntity>> = combine(allPickups, currentUser) { pickups, user ->
        if (user != null && user.role == UserRole.COLLECTOR.name) {
            pickups.filter { it.collectorId == user.id }
        } else emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeNearbyPickups: StateFlow<List<PickupRequestEntity>> = allPickups.map { pickups ->
        pickups.filter {
            it.status == PickupStatus.ACCEPTED.name ||
            it.status == PickupStatus.PICKUP_SCHEDULED.name ||
            it.status == PickupStatus.REQUESTED.name
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Collector base / current GPS coordinates (defaults to Dharavi, Mumbai)
    private val _collectorLocation = MutableStateFlow<Pair<Double, Double>>(Pair(19.0434, 72.8562))
    val collectorLocation: StateFlow<Pair<Double, Double>> = _collectorLocation.asStateFlow()

    // Route Optimization Result
    private val _optimizedRoute = MutableStateFlow<RouteOptimizationResult?>(null)
    val optimizedRoute: StateFlow<RouteOptimizationResult?> = _optimizedRoute.asStateFlow()

    val recyclerPickups: StateFlow<List<PickupRequestEntity>> = combine(allPickups, currentUser) { pickups, user ->
        if (user != null && user.role == UserRole.RECYCLER.name) {
            pickups.filter { it.recyclerId == user.id }
        } else emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val collectorTransactions: StateFlow<List<TransactionRecordEntity>> = combine(allTransactions, currentUser) { txns, user ->
        if (user != null && user.role == UserRole.COLLECTOR.name) {
            txns.filter { it.collectorId == user.id }
        } else emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recyclerTransactions: StateFlow<List<TransactionRecordEntity>> = combine(allTransactions, currentUser) { txns, user ->
        if (user != null && user.role == UserRole.RECYCLER.name) {
            txns.filter { it.recyclerId == user.id }
        } else emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Notifications
    val notifications: StateFlow<List<NotificationEntity>> = currentUser.flatMapLatest { user ->
        if (user != null) {
            repository.getNotificationsFlow(user.id, user.role)
        } else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Wizard State for E-Waste Collection
    private val _lotCreationState = MutableStateFlow(LotCreationState())
    val lotCreationState: StateFlow<LotCreationState> = _lotCreationState.asStateFlow()

    // Notification toast / alert message
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    init {
        LocalizationManager.initialize(application)
        audioManager.setLocaleForLanguage(LocalizationManager.currentLanguage.value)
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
    }

    fun showMessage(msg: String) {
        _userMessage.value = msg
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    fun setLanguage(lang: AppLanguage, speakGreeting: Boolean = true) {
        audioManager.stop()
        LocalizationManager.setLanguage(lang, getApplication())
        audioManager.setLocaleForLanguage(lang)
        if (speakGreeting) {
            audioManager.speakLanguageChangedGreeting(lang)
        }
    }

    fun speak(text: String) {
        audioManager.speak(text)
    }

    fun speakFeedback(text: String) {
        audioManager.speakFeedback(text)
    }

    fun retryAudio() {
        audioManager.retry()
    }

    fun dismissVoiceWarning() {
        audioManager.clearVoiceWarning()
    }

    fun pauseAudio() {
        audioManager.pause()
    }

    fun resumeAudio() {
        audioManager.resume()
    }

    fun stopAudio() {
        audioManager.stop()
    }

    fun replayAudio() {
        audioManager.replay()
    }

    fun setSpeechRate(rate: Float) {
        audioManager.setSpeechRate(rate)
    }

    fun toggleOnlineSimulation() {
        val newState = !repository.isOnline.value
        repository.setOnlineMode(newState)
        if (newState) {
            triggerSync()
        } else {
            showMessage(LocalizationManager.getString("offline_mode"))
        }
    }

    fun triggerSync() {
        viewModelScope.launch {
            val count = repository.synchronizeOfflineQueue()
            showMessage("✓ $count ${LocalizationManager.getString("sync_now")}")
        }
    }

    // --- Authentication ---
    fun login(identifier: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val res = repository.login(identifier, pass)
            if (res.isSuccess) {
                onSuccess()
            } else {
                onError(res.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }

    fun quickLogin(role: UserRole, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val res = repository.quickLoginAs(role)
            if (res.isSuccess) {
                onSuccess()
            } else {
                showMessage("Could not login as ${role.name}")
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        repository.logout()
        stopAudio()
        onLoggedOut()
    }

    fun resetPassword(identifier: String, newPass: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val res = repository.resetPassword(identifier, newPass)
            if (res.isSuccess) {
                onResult(true, res.getOrNull() ?: "Password successfully reset")
            } else {
                onResult(false, res.exceptionOrNull()?.message ?: "Failed to reset password")
            }
        }
    }

    // --- Registration ---
    fun registerCollector(
        fullName: String,
        phone: String,
        username: String,
        pass: String,
        address: String,
        city: String,
        state: String,
        pincode: String,
        govtId: String,
        area: String,
        languages: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val userId = "col_${System.currentTimeMillis()}"
            val user = UserEntity(
                id = userId,
                username = username,
                phone = phone,
                passwordHash = pass,
                role = UserRole.COLLECTOR.name,
                status = UserStatus.PENDING.name
            )
            val profile = CollectorProfileEntity(
                userId = userId,
                fullName = fullName,
                phone = phone,
                address = address,
                city = city,
                state = state,
                pincode = pincode,
                gpsLat = 19.0760,
                gpsLng = 72.8777,
                govtIdNumber = govtId,
                preferredArea = area,
                languagesKnown = languages
            )
            repository.registerCollector(user, profile)
            onSuccess()
        }
    }

    fun registerRecycler(
        orgName: String,
        personName: String,
        phone: String,
        username: String,
        pass: String,
        address: String,
        city: String,
        state: String,
        pincode: String,
        certNumber: String,
        certAuthority: String,
        certIssueDate: String,
        certExpiryDate: String,
        acceptedCategories: String,
        capacity: Double,
        minQty: Double,
        pickupRadius: Double,
        ratesJson: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val userId = "rec_${System.currentTimeMillis()}"
            val user = UserEntity(
                id = userId,
                username = username,
                phone = phone,
                passwordHash = pass,
                role = UserRole.RECYCLER.name,
                status = UserStatus.PENDING.name
            )
            val profile = RecyclerProfileEntity(
                userId = userId,
                orgName = orgName,
                authorizedPerson = personName,
                phone = phone,
                address = address,
                city = city,
                state = state,
                pincode = pincode,
                gpsLat = 19.0760,
                gpsLng = 72.8777,
                certNumber = certNumber,
                certAuthority = certAuthority,
                certIssueDate = certIssueDate,
                certExpiryDate = certExpiryDate,
                certDocumentUri = "https://cpcb.nic.in/doc/$certNumber",
                certStatus = RecyclerCertStatus.PENDING_VERIFICATION.name,
                acceptedCategoriesCsv = acceptedCategories,
                processingCapacityKgPerDay = capacity,
                minQuantityKg = minQty,
                pickupRadiusKm = pickupRadius,
                pickupAvailable = true,
                ratesJson = ratesJson
            )
            repository.registerRecycler(user, profile)
            onSuccess()
        }
    }

    // --- Collect E-Waste Wizard ---
    fun startLotCreation() {
        _lotCreationState.value = LotCreationState(
            step = 1,
            weightInput = "12.0",
            location = "Dharavi E-Hub, Mumbai",
            lat = 19.0434,
            lng = 72.8562
        )
    }

    fun updateLotInput(weight: String, desc: String, loc: String, photo: String) {
        _lotCreationState.update {
            it.copy(
                weightInput = weight,
                description = desc,
                location = loc,
                photoUri = photo
            )
        }
    }

    fun runAiClassification() {
        val current = _lotCreationState.value
        val weight = current.weightInput.toDoubleOrNull() ?: 5.0
        viewModelScope.launch {
            val result = classificationService.classifyMaterial(
                photoUri = current.photoUri,
                description = current.description,
                weightKg = weight,
                location = current.location
            )
            _lotCreationState.update {
                it.copy(
                    step = 2,
                    aiClassification = result,
                    selectedCategory = result.category,
                    selectedSubcategory = result.subcategory,
                    isAiConfirmed = true
                )
            }
            speak("AI Classification: ${result.category}. Confidence: ${result.confidence} percent. Is this classification correct?")
        }
    }

    fun confirmAiClassification(isCorrect: Boolean, newCategory: String? = null, newSub: String? = null) {
        if (isCorrect) {
            _lotCreationState.update { it.copy(step = 3) }
            speak("Please select the material condition: Working, Partially Working, Damaged, Non-Working, or Scrap.")
        } else if (newCategory != null) {
            _lotCreationState.update {
                it.copy(
                    selectedCategory = newCategory,
                    selectedSubcategory = newSub ?: newCategory,
                    step = 3
                )
            }
            speak("Category changed to $newCategory. Please select material condition.")
        }
    }

    fun selectCondition(condition: MaterialCondition) {
        _lotCreationState.update { it.copy(selectedCondition = condition) }
        speak("${condition.displayName}. ${condition.audioDescription}")
    }

    fun runPriceEstimation() {
        val current = _lotCreationState.value
        val weight = current.weightInput.toDoubleOrNull() ?: 10.0
        _lotCreationState.update { it.copy(isEstimatingPrice = true) }

        viewModelScope.launch {
            val result = priceEstimationService.estimatePrice(
                category = current.selectedCategory,
                subCategory = current.selectedSubcategory,
                condition = current.selectedCondition,
                weightKg = weight,
                location = current.location
            )
            _lotCreationState.update {
                it.copy(
                    priceEstimate = result,
                    isEstimatingPrice = false,
                    step = 4
                )
            }
            speak("Estimated Price: ₹${result.estimatedPricePerKg.toInt()} per kilogram. Total Lot Value: ₹${result.estimatedTotalValue.toInt()}. This is an AI/ML estimated price.")
        }
    }

    fun findRecyclerMatches() {
        val current = _lotCreationState.value
        val weight = current.weightInput.toDoubleOrNull() ?: 10.0
        _lotCreationState.update { it.copy(isMatching = true) }

        viewModelScope.launch {
            val approved = repository.getApprovedRecyclers()
            val matches = recyclerMatchingService.findMatchingRecyclers(
                category = current.selectedCategory,
                weightKg = weight,
                collectorLat = current.lat,
                collectorLng = current.lng,
                allRecyclers = approved
            )
            _lotCreationState.update {
                it.copy(
                    matchingRecyclers = matches,
                    selectedRecyclerMatch = matches.firstOrNull(),
                    isMatching = false,
                    step = 5
                )
            }
            speak("Found ${matches.size} authorized recyclers matching your lot. Select a recycler to request pickup.")
        }
    }

    fun selectRecyclerMatch(match: RecyclerMatchResult) {
        _lotCreationState.update { it.copy(selectedRecyclerMatch = match) }
    }

    fun finalizeLotAndCreatePickup(onComplete: () -> Unit) {
        val current = _lotCreationState.value
        val user = currentUser.value ?: return
        val profile = currentCollectorProfile.value
        val match = current.selectedRecyclerMatch ?: return
        val weight = current.weightInput.toDoubleOrNull() ?: 10.0
        val lotId = "LOT-${System.currentTimeMillis() % 1000000}"

        viewModelScope.launch {
            val lot = MaterialLotEntity(
                lotId = lotId,
                collectorId = user.id,
                dateTime = System.currentTimeMillis(),
                photoUri = current.photoUri,
                description = current.description.ifBlank { "${current.selectedCategory} lot" },
                weightKg = weight,
                locationName = current.location,
                gpsLat = current.lat,
                gpsLng = current.lng,
                category = current.selectedCategory,
                subCategory = current.selectedSubcategory,
                aiConfidence = current.aiClassification?.confidence ?: 92,
                condition = current.selectedCondition.name,
                estimatedPricePerKg = match.offeredPricePerKg,
                estimatedTotalValue = match.totalEstimatedPayout,
                matchedRecyclerId = match.recycler.userId,
                matchedRecyclerName = match.recycler.orgName,
                status = LotStatus.PICKUP_REQUESTED.name
            )
            repository.createMaterialLot(lot)

            val pickup = PickupRequestEntity(
                lotId = lotId,
                collectorId = user.id,
                collectorName = profile?.fullName ?: user.username,
                collectorPhone = profile?.phone ?: user.phone,
                recyclerId = match.recycler.userId,
                recyclerName = match.recycler.orgName,
                materialCategory = current.selectedCategory,
                weightKg = weight,
                agreedPricePerKg = match.offeredPricePerKg,
                totalValue = match.totalEstimatedPayout,
                pickupAddress = profile?.address ?: current.location,
                gpsLat = current.lat,
                gpsLng = current.lng,
                preferredDateTime = "Tomorrow, Morning",
                status = PickupStatus.REQUESTED.name
            )
            repository.createPickupRequest(pickup)

            _lotCreationState.update {
                it.copy(
                    step = 6,
                    generatedLotId = lotId
                )
            }
            speak("Success! Material lot $lotId submitted and pickup requested from ${match.recycler.orgName}.")
            onComplete()
        }
    }

    // --- Recycler Actions ---
    fun acceptPickupRequest(request: PickupRequestEntity) {
        viewModelScope.launch {
            repository.updatePickupStatus(request.id, PickupStatus.ACCEPTED)
            showMessage("Pickup request accepted. Notified collector.")
        }
    }

    fun rejectPickupRequest(request: PickupRequestEntity) {
        viewModelScope.launch {
            repository.updatePickupStatus(request.id, PickupStatus.REJECTED)
            showMessage("Pickup request declined.")
        }
    }

    fun schedulePickup(request: PickupRequestEntity) {
        viewModelScope.launch {
            repository.updatePickupStatus(request.id, PickupStatus.PICKUP_SCHEDULED)
            showMessage("Pickup scheduled.")
        }
    }

    // --- Digital Handover ---
    fun confirmHandoverAndPay(
        request: PickupRequestEntity,
        actualWeight: Double,
        onComplete: (String) -> Unit
    ) {
        viewModelScope.launch {
            val refId = "KBC-${java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)}-${(100000..999999).random()}"
            val handover = HandoverRecordEntity(
                referenceId = refId,
                lotId = request.lotId,
                pickupRequestId = request.id,
                collectorId = request.collectorId,
                recyclerId = request.recyclerId,
                actualWeightKg = actualWeight,
                photoUri = "",
                gpsLat = request.gpsLat,
                gpsLng = request.gpsLng,
                confirmedByCollector = true,
                confirmedByRecycler = true
            )
            val txnId = repository.recordHandoverAndCompleteTransaction(
                handover = handover,
                pricePerKg = request.agreedPricePerKg,
                collectorName = request.collectorName,
                recyclerName = request.recyclerName,
                category = request.materialCategory,
                location = request.pickupAddress
            )
            showMessage("Handover confirmed! Txn Ref: $refId")
            onComplete(refId)
        }
    }

    // --- Admin Actions ---
    fun approveCollector(userId: String) {
        viewModelScope.launch {
            repository.updateCollectorApproval(userId, true)
            showMessage("Collector approved successfully.")
        }
    }

    fun rejectCollector(userId: String) {
        viewModelScope.launch {
            repository.updateCollectorApproval(userId, false)
            showMessage("Collector registration rejected.")
        }
    }

    fun approveRecycler(userId: String) {
        viewModelScope.launch {
            repository.updateRecyclerAuthorization(userId, true)
            showMessage("Recycler authorization verified and approved!")
        }
    }

    fun rejectRecycler(userId: String) {
        viewModelScope.launch {
            repository.updateRecyclerAuthorization(userId, false)
            showMessage("Recycler certificate rejected.")
        }
    }

    fun markTransactionPaid(txnId: String) {
        viewModelScope.launch {
            repository.updateTransactionPayment(txnId, PaymentStatus.PAID, "Payment processed via UPI/Bank transfer.")
            showMessage("Payment marked as PAID.")
        }
    }

    fun markTransactionDisputed(txnId: String, reason: String) {
        viewModelScope.launch {
            repository.updateTransactionPayment(txnId, PaymentStatus.DISPUTED, "Dispute raised: $reason")
            showMessage("Transaction marked as DISPUTED.")
        }
    }

    fun updateRecyclerSettings(
        ratesJson: String,
        categories: String,
        minQty: Double,
        pickupAvail: Boolean,
        radius: Double
    ) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            repository.updateRecyclerSettings(user.id, ratesJson, categories, minQty, pickupAvail, radius)
            showMessage("Rates and pickup settings updated.")
        }
    }

    fun updateCollectorLocation(lat: Double, lng: Double) {
        _collectorLocation.value = Pair(lat, lng)
    }

    fun calculateDistanceKm(targetLat: Double, targetLng: Double): Double {
        val (cLat, cLng) = _collectorLocation.value
        val results = FloatArray(1)
        return try {
            android.location.Location.distanceBetween(cLat, cLng, targetLat, targetLng, results)
            Math.round((results[0] / 1000.0) * 10.0) / 10.0
        } catch (e: Exception) {
            0.0
        }
    }

    fun optimizeRouteForPickups(pickupsToOptimize: List<PickupRequestEntity>): RouteOptimizationResult {
        val (startLat, startLng) = _collectorLocation.value
        if (pickupsToOptimize.isEmpty()) {
            val empty = RouteOptimizationResult()
            _optimizedRoute.value = empty
            return empty
        }

        val unvisited = pickupsToOptimize.toMutableList()
        val orderedStops = mutableListOf<PickupRequestEntity>()
        var curLat = startLat
        var curLng = startLng
        var totalDistKm = 0.0

        val polyline = mutableListOf<Pair<Double, Double>>()
        polyline.add(Pair(startLat, startLng))

        while (unvisited.isNotEmpty()) {
            var bestIdx = 0
            var minDist = Double.MAX_VALUE
            for (i in unvisited.indices) {
                val results = FloatArray(1)
                try {
                    android.location.Location.distanceBetween(curLat, curLng, unvisited[i].gpsLat, unvisited[i].gpsLng, results)
                    val dist = (results[0] / 1000.0).toDouble()
                    if (dist < minDist) {
                        minDist = dist
                        bestIdx = i
                    }
                } catch (e: Exception) {
                    // Ignore calculation error
                }
            }
            val stop = unvisited.removeAt(bestIdx)
            orderedStops.add(stop)
            totalDistKm += minDist
            curLat = stop.gpsLat
            curLng = stop.gpsLng
            polyline.add(Pair(curLat, curLng))
        }

        val roundedDist = Math.round(totalDistKm * 10.0) / 10.0
        val estDuration = (roundedDist * 2.7).toInt() + (orderedStops.size * 12)
        val totalWeight = orderedStops.sumOf { it.weightKg }
        val totalVal = orderedStops.sumOf { it.totalValue }

        val result = RouteOptimizationResult(
            stops = orderedStops,
            polylinePoints = polyline,
            totalDistanceKm = roundedDist,
            estimatedDurationMinutes = estDuration,
            totalWeightKg = totalWeight,
            totalEstimatedValue = totalVal
        )
        _optimizedRoute.value = result
        return result
    }

    fun clearOptimizedRoute() {
        _optimizedRoute.value = null
    }
}
