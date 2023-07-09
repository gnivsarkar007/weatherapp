package com.rtl.android.assignment.repository

import com.rtl.android.assignment.cache.database.entity.CityEntity
import kotlinx.coroutines.flow.Flow

interface CitiesRepo {
    fun getAllCities(): Flow<Result<List<CityEntity>>>
    suspend fun getCity(id: String): CityEntity
    suspend fun addCity(city: CityEntity)
    suspend fun removeCity(id: String)
}