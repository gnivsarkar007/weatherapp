package com.rtl.android.assignment.api

import com.rtl.android.assignment.BuildConfig
import com.rtl.android.assignment.api.model.ApiResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {

    @GET("onecall?units=metric&appid=${BuildConfig.WEATHERS_API_KEY}")
    suspend fun getWeatherInfo(
        @Query("lat") lat: Double,
        @Query("lon") long: Double
    ): Response<ApiResponse>

}

class WeatherServiceImpl(retrofit: Retrofit) : WeatherService {
    private val service = retrofit.create(WeatherService::class.java)

    override suspend fun getWeatherInfo(lat: Double, long: Double): Response<ApiResponse> =
        service.getWeatherInfo(lat, long)

}