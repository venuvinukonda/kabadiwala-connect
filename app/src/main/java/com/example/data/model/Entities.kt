package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val username: String,
    val phone: String,
    val passwordHash: String,
    val role: String, // COLLECTOR, RECYCLER, ADMIN
    val status: String, // PENDING, APPROVED, REJECTED, SUSPENDED
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "collector_profiles")
data class CollectorProfileEntity(
    @PrimaryKey val userId: String,
    val fullName: String,
    val phone: String,
    val address: String,
    val city: String,
    val state: String,
    val pincode: String,
    val gpsLat: Double,
    val gpsLng: Double,
    val govtIdNumber: String,
    val preferredArea: String,
    val languagesKnown: String,
    val photoUrl: String = ""
)

@Entity(tableName = "recycler_profiles")
data class RecyclerProfileEntity(
    @PrimaryKey val userId: String,
    val orgName: String,
    val authorizedPerson: String,
    val phone: String,
    val address: String,
    val city: String,
    val state: String,
    val pincode: String,
    val gpsLat: Double,
    val gpsLng: Double,
    val certNumber: String,
    val certAuthority: String,
    val certIssueDate: String,
    val certExpiryDate: String,
    val certDocumentUri: String,
    val certStatus: String, // PENDING_VERIFICATION, APPROVED, REJECTED, EXPIRED, SUSPENDED
    val acceptedCategoriesCsv: String,
    val processingCapacityKgPerDay: Double,
    val minQuantityKg: Double,
    val pickupRadiusKm: Double,
    val pickupAvailable: Boolean,
    val ratesJson: String
)

@Entity(tableName = "material_lots")
data class MaterialLotEntity(
    @PrimaryKey val lotId: String,
    val collectorId: String,
    val dateTime: Long = System.currentTimeMillis(),
    val photoUri: String = "",
    val description: String,
    val weightKg: Double,
    val locationName: String,
    val gpsLat: Double,
    val gpsLng: Double,
    val category: String,
    val subCategory: String,
    val aiConfidence: Int,
    val condition: String,
    val estimatedPricePerKg: Double,
    val estimatedTotalValue: Double,
    val matchedRecyclerId: String = "",
    val matchedRecyclerName: String = "",
    val status: String, // DRAFT, CLASSIFIED, MATCHED, PICKUP_REQUESTED, COMPLETED
    val isSynced: Boolean = true
)

@Entity(tableName = "pickup_requests")
data class PickupRequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val lotId: String,
    val collectorId: String,
    val collectorName: String,
    val collectorPhone: String,
    val recyclerId: String,
    val recyclerName: String,
    val materialCategory: String,
    val weightKg: Double,
    val agreedPricePerKg: Double,
    val totalValue: Double,
    val pickupAddress: String,
    val gpsLat: Double,
    val gpsLng: Double,
    val preferredDateTime: String,
    val status: String, // REQUESTED, ACCEPTED, PICKUP_SCHEDULED, PICKED_UP, HANDOVER_CONFIRMED, COMPLETED, REJECTED
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "handover_records")
data class HandoverRecordEntity(
    @PrimaryKey val referenceId: String, // e.g. KBC-2026-000124
    val lotId: String,
    val pickupRequestId: Long,
    val collectorId: String,
    val recyclerId: String,
    val actualWeightKg: Double,
    val photoUri: String,
    val gpsLat: Double,
    val gpsLng: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val confirmedByCollector: Boolean,
    val confirmedByRecycler: Boolean
)

@Entity(tableName = "transaction_records")
data class TransactionRecordEntity(
    @PrimaryKey val transactionId: String, // e.g. TXN-2026-1042
    val lotId: String,
    val handoverRefId: String,
    val collectorId: String,
    val collectorName: String,
    val recyclerId: String,
    val recyclerName: String,
    val category: String,
    val weightKg: Double,
    val pricePerKg: Double,
    val totalAmount: Double,
    val dateTimestamp: Long = System.currentTimeMillis(),
    val location: String,
    val handoverStatus: String,
    val paymentStatus: String, // PENDING, RECORDED, PAID, FAILED, DISPUTED
    val notes: String = ""
)

@Entity(tableName = "sync_queue")
data class SyncQueueEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val actionType: String, // CREATE_LOT, UPDATE_PICKUP, RECORD_HANDOVER, SYNC_PROFILE
    val payloadJson: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "PENDING" // PENDING, SYNCED, FAILED
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recipientUserId: String,
    val role: String, // ALL, COLLECTOR, RECYCLER, ADMIN
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val actorUserId: String,
    val actorRole: String,
    val action: String,
    val details: String,
    val timestamp: Long = System.currentTimeMillis()
)
