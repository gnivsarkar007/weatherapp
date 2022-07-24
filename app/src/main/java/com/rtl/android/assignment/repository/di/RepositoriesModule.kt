package com.rtl.android.assignment.repository.di

import com.rtl.android.assignment.api.WeatherService
import com.rtl.android.assignment.cache.database.dao.CitiesDao
import com.rtl.android.assignment.repository.CitiesRepo
import com.rtl.android.assignment.repository.CitiesRepoImpl
import com.rtl.android.assignment.repository.WeatherRepo
import com.rtl.android.assignment.repository.WeatherRepoImpl
import com.rtl.android.assignment.util.IoDispatcher
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
class RepositoriesModule {
    @Provides
    @Singleton
    fun providesWeatherRepository(
        citiesDao: CitiesDao,
        apiService: WeatherService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): WeatherRepo =
        WeatherRepoImpl(apiService, citiesDao, dispatcher = ioDispatcher)

    @Provides
    @Singleton
    fun providesCitiesRepo(
        citiesDao: CitiesDao,
    ): CitiesRepo = CitiesRepoImpl(citiesDao)
}