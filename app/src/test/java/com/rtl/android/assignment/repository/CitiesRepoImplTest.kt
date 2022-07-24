package com.rtl.android.assignment.repository

import com.rtl.android.assignment.cache.database.dao.CitiesDao
import com.rtl.android.assignment.cache.database.entity.CityEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CitiesRepoImplTest {
    private val cityDao: CitiesDao = mock()
    private val repo = CitiesRepoImpl(cityDao)
    private val dispatcher = StandardTestDispatcher(scheduler = TestCoroutineScheduler())

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `test get all cities`() = runTest {
        val expected = entities()
        whenever(cityDao.getCities()).thenReturn(flow { emit(expected) })
        val result = repo.getAllCities().first()
        advanceUntilIdle()
        assert((result is Result.Success<List<CityEntity>>))
        (result as Result.Success<List<CityEntity>>).data.forEachIndexed { index, cityEntity ->
            assert(cityEntity == expected[index])
        }
    }

    @Test
    fun `test get city`() = runTest {
        repo.getCity("title")
        verify(cityDao).getDetails("title")
    }

    @Test
    fun `test add city`() = runTest {
        repo.addCity("title", 1.0, 2.0)
        verify(cityDao).insert(eq(CityEntity("title", 1.0, 2.0)))
    }

    @Test
    fun `test remove city`() = runTest {
        repo.removeCity("title")
        verify(cityDao).remove("title")
    }


    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    private fun entities() = mutableListOf<CityEntity>().also {
        for (i in 1..5) {
            it.add(
                CityEntity(
                    "title$i",
                    10.0 + i,
                    20.0 + i
                )
            )
        }
    }
}