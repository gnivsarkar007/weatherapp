package com.rtl.android.assignment.cache.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rtl.android.assignment.cache.database.entity.CityEntity
import com.rtl.android.assignment.cache.database.entity.DELETE
import com.rtl.android.assignment.cache.database.entity.SELECT
import kotlinx.coroutines.flow.Flow

@Dao
interface CitiesDao {
    @Query(value = SELECT)
    fun getCities(): Flow<List<CityEntity>>

    @Query(value = "$SELECT where name = ${":name"}")
    suspend fun getDetails(name: String): CityEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(city: CityEntity)

    @Query(value = "$DELETE${":name"}")
    suspend fun remove(name: String)
}