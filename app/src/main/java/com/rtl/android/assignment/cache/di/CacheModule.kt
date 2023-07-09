package com.rtl.android.assignment.cache.di

import com.rtl.android.assignment.cache.database.AppDatabase
import com.rtl.android.assignment.cache.database.dao.CitiesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CacheModule {
    @Provides
    @Singleton
    fun providesAstroPicturesDao(db: AppDatabase): CitiesDao = db.citiesDao()
}