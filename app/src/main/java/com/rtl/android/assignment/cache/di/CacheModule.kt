package com.rtl.android.assignment.cache.di

import com.rtl.android.assignment.cache.database.AppDatabase
import com.rtl.android.assignment.cache.database.dao.CitiesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CacheModule {
    @Provides
    @Singleton
    fun providesAstroPicturesDao(db: AppDatabase): CitiesDao = db.citiesDao()
}