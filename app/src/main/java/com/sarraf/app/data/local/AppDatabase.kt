package com.sarraf.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sarraf.app.data.local.dao.ExchangeRateDao
import com.sarraf.app.data.local.entity.ExchangeRateEntity
import com.sarraf.app.data.local.entity.RateAlertEntity

@Database(
    entities = [
        ExchangeRateEntity::class,
        RateAlertEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exchangeRateDao(): ExchangeRateDao
}
