package com.rtl.android.assignment

import com.rtl.android.assignment.api.WeatherServiceImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


@OptIn(ExperimentalCoroutinesApi::class)
class WeatherServiceTest {

    /**
     * Integration test -
     */
    @Test
    fun testResponseCode() = runTest {
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

        val response = WeatherServiceImpl(retrofit).getWeatherInfo(lat = 33.44, long = -94.04)
        assert(response.isSuccessful)
    }
}
