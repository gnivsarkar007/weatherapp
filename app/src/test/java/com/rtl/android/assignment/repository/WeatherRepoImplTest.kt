package com.rtl.android.assignment.repository

import com.rtl.android.assignment.api.WeatherService
import com.rtl.android.assignment.cache.database.dao.CitiesDao
import com.rtl.android.assignment.cache.database.entity.CityEntity
import com.rtl.android.assignment.util.FileReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class WeatherRepoImplTest {
    private val dispatcher = StandardTestDispatcher(scheduler = TestCoroutineScheduler())
    private val apiService: WeatherService = mock()
    private val citiesDao: CitiesDao = mock()

    lateinit var repo: WeatherRepo

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repo = WeatherRepoImpl(apiService, citiesDao, dispatcher)
    }

    @Test
    fun `test get data success`() = runTest {
        whenever(citiesDao.getDetails("title")).thenReturn(CityEntity("title", 1.0, 2.0))
        val correctResponse = FileReader.apiResponse()!!
        whenever(apiService.getWeatherInfo(1.0, 2.0)).thenReturn(Response.success(correctResponse))
        val result = repo.getWeather("title")
        advanceUntilIdle()
        assert(result is Result.Success)
    }

    @Test
    fun `test get data failure`() = runTest {
        whenever(citiesDao.getDetails("title")).thenReturn(CityEntity("title", 1.0, 2.0))
        whenever(apiService.getWeatherInfo(1.0, 2.0)).thenReturn(Response.error(400, object :
            ResponseBody() {
            override fun contentLength(): Long = 0L

            override fun contentType(): MediaType? = null

            override fun source(): BufferedSource = Buffer()
        }))
        val result = repo.getWeather("title")
        advanceUntilIdle()
        assert((result as Result.Error).type == Result.Error.Type.API)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

}
