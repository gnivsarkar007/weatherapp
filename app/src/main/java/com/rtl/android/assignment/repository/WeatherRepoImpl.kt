package com.rtl.android.assignment.repository

import com.rtl.android.assignment.api.WeatherService
import com.rtl.android.assignment.api.model.ApiResponse
import com.rtl.android.assignment.cache.database.dao.CitiesDao
import com.rtl.android.assignment.util.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class WeatherRepoImpl @Inject constructor(
    private val apiService: WeatherService,
    private val citiesDao: CitiesDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : WeatherRepo {

    override suspend fun getWeather(
        cityName: String
    ): Result<ApiResponse> = safeApiCall(dispatcher) {
        val city = citiesDao.getDetails(cityName)
        apiService.getWeatherInfo(
            city.latitude, city.longitude
        )
    }

}
