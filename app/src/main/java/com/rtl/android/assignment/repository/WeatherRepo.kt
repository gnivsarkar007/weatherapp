package com.rtl.android.assignment.repository

import com.rtl.android.assignment.api.model.ApiResponse


interface WeatherRepo {
    suspend fun getWeather(cityName: String): Result<ApiResponse>
}


