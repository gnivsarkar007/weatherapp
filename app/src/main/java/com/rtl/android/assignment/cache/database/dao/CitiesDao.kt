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

    @Query(value = "$SELECT where name = ${":id"}")
    suspend fun getDetails(id: String): CityEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(city: CityEntity)

    @Query(value = "$DELETE${":id"}")
    suspend fun remove(id: String)
}