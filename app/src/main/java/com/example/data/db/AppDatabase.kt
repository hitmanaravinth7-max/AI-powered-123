package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.BusinessProfileEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.ExpenseRecordEntity
import com.example.data.model.MarketingMetricEntity
import com.example.data.model.RecommendationEntity
import com.example.data.model.SalesRecordEntity
import com.example.data.model.UserEntity

@Database(
    entities = [
        UserEntity::class,
        BusinessProfileEntity::class,
        SalesRecordEntity::class,
        ExpenseRecordEntity::class,
        MarketingMetricEntity::class,
        RecommendationEntity::class,
        ChatMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun businessDao(): BusinessDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "biz_consultant_database"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
