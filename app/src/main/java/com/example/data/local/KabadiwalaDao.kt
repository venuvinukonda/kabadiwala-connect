package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface KabadiwalaDao {

    // --- Users ---
    @Query("SELECT * FROM users WHERE (username = :identifier OR phone = :identifier) LIMIT 1")
    suspend fun findUserByIdentifier(identifier: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: String): UserEntity?

    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    fun getAllUsersFlow(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("UPDATE users SET status = :status WHERE id = :userId")
    suspend fun updateUserStatus(userId: String, status: String)

    @Query("UPDATE users SET passwordHash = :newPassword WHERE id = :userId")
    suspend fun updateUserPassword(userId: String, newPassword: String)

    // --- Collector Profiles ---
    @Query("SELECT * FROM collector_profiles WHERE userId = :userId LIMIT 1")
    suspend fun getCollectorProfile(userId: String): CollectorProfileEntity?

    @Query("SELECT * FROM collector_profiles")
    fun getAllCollectorProfilesFlow(): Flow<List<CollectorProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollectorProfile(profile: CollectorProfileEntity)

    // --- Recycler Profiles ---
    @Query("SELECT * FROM recycler_profiles WHERE userId = :userId LIMIT 1")
    suspend fun getRecyclerProfile(userId: String): RecyclerProfileEntity?

    @Query("SELECT * FROM recycler_profiles")
    fun getAllRecyclerProfilesFlow(): Flow<List<RecyclerProfileEntity>>

    @Query("SELECT * FROM recycler_profiles WHERE certStatus = 'APPROVED'")
    suspend fun getApprovedRecyclers(): List<RecyclerProfileEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecyclerProfile(profile: RecyclerProfileEntity)

    @Query("UPDATE recycler_profiles SET certStatus = :status WHERE userId = :userId")
    suspend fun updateRecyclerCertStatus(userId: String, status: String)

    @Query("UPDATE recycler_profiles SET ratesJson = :ratesJson, acceptedCategoriesCsv = :categories, minQuantityKg = :minQty, pickupAvailable = :pickupAvail, pickupRadiusKm = :radius WHERE userId = :userId")
    suspend fun updateRecyclerSettings(userId: String, ratesJson: String, categories: String, minQty: Double, pickupAvail: Boolean, radius: Double)

    // --- Material Lots ---
    @Query("SELECT * FROM material_lots ORDER BY dateTime DESC")
    fun getAllLotsFlow(): Flow<List<MaterialLotEntity>>

    @Query("SELECT * FROM material_lots WHERE collectorId = :collectorId ORDER BY dateTime DESC")
    fun getLotsByCollectorFlow(collectorId: String): Flow<List<MaterialLotEntity>>

    @Query("SELECT * FROM material_lots WHERE lotId = :lotId LIMIT 1")
    suspend fun getLotById(lotId: String): MaterialLotEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterialLot(lot: MaterialLotEntity)

    @Update
    suspend fun updateMaterialLot(lot: MaterialLotEntity)

    @Query("SELECT * FROM material_lots WHERE isSynced = 0")
    suspend fun getUnsyncedLots(): List<MaterialLotEntity>

    @Query("UPDATE material_lots SET isSynced = 1 WHERE lotId = :lotId")
    suspend fun markLotSynced(lotId: String)

    // --- Pickup Requests ---
    @Query("SELECT * FROM pickup_requests ORDER BY createdAt DESC")
    fun getAllPickupRequestsFlow(): Flow<List<PickupRequestEntity>>

    @Query("SELECT * FROM pickup_requests WHERE collectorId = :collectorId ORDER BY createdAt DESC")
    fun getPickupRequestsForCollectorFlow(collectorId: String): Flow<List<PickupRequestEntity>>

    @Query("SELECT * FROM pickup_requests WHERE recyclerId = :recyclerId ORDER BY createdAt DESC")
    fun getPickupRequestsForRecyclerFlow(recyclerId: String): Flow<List<PickupRequestEntity>>

    @Query("SELECT * FROM pickup_requests WHERE id = :id LIMIT 1")
    suspend fun getPickupRequestById(id: Long): PickupRequestEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPickupRequest(request: PickupRequestEntity): Long

    @Query("UPDATE pickup_requests SET status = :status WHERE id = :id")
    suspend fun updatePickupStatus(id: Long, status: String)

    // --- Handover Records ---
    @Query("SELECT * FROM handover_records ORDER BY timestamp DESC")
    fun getAllHandoversFlow(): Flow<List<HandoverRecordEntity>>

    @Query("SELECT * FROM handover_records WHERE referenceId = :refId LIMIT 1")
    suspend fun getHandoverByRefId(refId: String): HandoverRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHandover(handover: HandoverRecordEntity)

    // --- Transactions ---
    @Query("SELECT * FROM transaction_records ORDER BY dateTimestamp DESC")
    fun getAllTransactionsFlow(): Flow<List<TransactionRecordEntity>>

    @Query("SELECT * FROM transaction_records WHERE collectorId = :collectorId ORDER BY dateTimestamp DESC")
    fun getTransactionsForCollectorFlow(collectorId: String): Flow<List<TransactionRecordEntity>>

    @Query("SELECT * FROM transaction_records WHERE recyclerId = :recyclerId ORDER BY dateTimestamp DESC")
    fun getTransactionsForRecyclerFlow(recyclerId: String): Flow<List<TransactionRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionRecordEntity)

    @Query("UPDATE transaction_records SET paymentStatus = :paymentStatus, notes = :notes WHERE transactionId = :txnId")
    suspend fun updateTransactionPayment(txnId: String, paymentStatus: String, notes: String)

    // --- Offline Sync Queue ---
    @Query("SELECT * FROM sync_queue WHERE status = 'PENDING' ORDER BY timestamp ASC")
    suspend fun getPendingSyncItems(): List<SyncQueueEntity>

    @Query("SELECT COUNT(*) FROM sync_queue WHERE status = 'PENDING'")
    fun getPendingSyncCountFlow(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncQueueItem(item: SyncQueueEntity)

    @Query("UPDATE sync_queue SET status = 'SYNCED' WHERE id = :id")
    suspend fun markSyncQueueItemSynced(id: Long)

    @Query("DELETE FROM sync_queue WHERE status = 'SYNCED'")
    suspend fun clearSyncedQueue()

    // --- Notifications ---
    @Query("SELECT * FROM notifications WHERE recipientUserId = :userId OR role = :role OR role = 'ALL' ORDER BY timestamp DESC")
    fun getNotificationsFlow(userId: String, role: String): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE recipientUserId = :userId OR role = :role")
    suspend fun markAllNotificationsRead(userId: String, role: String)

    // --- Audit Logs ---
    @Query("SELECT * FROM audit_logs ORDER BY timestamp DESC LIMIT 100")
    fun getAuditLogsFlow(): Flow<List<AuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: AuditLogEntity)
}
