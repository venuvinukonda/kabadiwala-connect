package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.*

@Database(
    entities = [
        UserEntity::class,
        CollectorProfileEntity::class,
        RecyclerProfileEntity::class,
        MaterialLotEntity::class,
        PickupRequestEntity::class,
        HandoverRecordEntity::class,
        TransactionRecordEntity::class,
        SyncQueueEntity::class,
        NotificationEntity::class,
        AuditLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class KabadiwalaDatabase : RoomDatabase() {

    abstract fun kabadiwalaDao(): KabadiwalaDao

    companion object {
        @Volatile
        private var INSTANCE: KabadiwalaDatabase? = null

        fun getDatabase(context: Context): KabadiwalaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KabadiwalaDatabase::class.java,
                    "kabadiwala_connect_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
