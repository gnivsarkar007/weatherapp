package com.rtl.android.assignment.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rtl.android.assignment.cache.database.dao.CitiesDao
import com.rtl.android.assignment.cache.database.entity.CityEntity


@Database(entities = [CityEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun citiesDao(): CitiesDao
}