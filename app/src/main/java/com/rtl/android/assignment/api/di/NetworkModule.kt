package com.rtl.android.assignment.api.di

import com.rtl.android.assignment.BuildConfig
import com.rtl.android.assignment.api.WeatherService
import com.rtl.android.assignment.api.WeatherServiceImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun providesWeatherService(): WeatherService {
        val moshi: Moshi = Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BuildConfig.WEATHERS_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }

        return WeatherServiceImpl(retrofit)
    }
}