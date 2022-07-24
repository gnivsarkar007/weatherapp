package com.rtl.android.assignment.cache.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rtl.android.assignment.cache.database.dao.CitiesDao
import com.rtl.android.assignment.cache.database.entity.CityEntity
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {
    private lateinit var citiesDao: CitiesDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        citiesDao = db.citiesDao()
    }

    @Test
    fun testDateFormattingWorksCorrectWithSort() = runTest {
        citiesDao.insert(
            CityEntity(
                "title",
                10.0,
                20.0
            )
        )

        citiesDao.getCities().first().forEachIndexed { index, localDate ->
            assertEquals(localDate, "title")
        }

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}