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
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
class RepositoriesModule {
    @Provides
    fun providesWeatherRepository(
        citiesDao: CitiesDao,
        apiService: WeatherService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): WeatherRepo =
        WeatherRepoImpl(apiService, citiesDao, dispatcher = ioDispatcher)

    @Provides
    fun providesCitiesRepo(
        citiesDao: CitiesDao,
    ): CitiesRepo = CitiesRepoImpl(citiesDao)
}