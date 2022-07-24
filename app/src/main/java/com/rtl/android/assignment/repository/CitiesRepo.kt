package com.rtl.android.assignment.repository

import com.rtl.android.assignment.cache.database.entity.CityEntity
import kotlinx.coroutines.flow.Flow

interface CitiesRepo {
    fun getAllCities(): Flow<Result<List<CityEntity>>>
    suspend fun getCity(name: String): CityEntity
    suspend fun addCity(name: String, latitude: Double, longitude: Double)
    suspend fun removeCity(name: String)
}