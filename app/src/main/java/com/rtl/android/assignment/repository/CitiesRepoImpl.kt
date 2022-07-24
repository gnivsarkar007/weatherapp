package com.rtl.android.assignment.repository

import com.rtl.android.assignment.cache.database.dao.CitiesDao
import com.rtl.android.assignment.cache.database.entity.CityEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class CitiesRepoImpl(
    private val cityDao: CitiesDao,
) : CitiesRepo {
    override fun getAllCities(): Flow<Result<List<CityEntity>>> = cityDao.getCities().map {
        Result.Success(it)
    }.catch { Result.Error(Result.Error.Type.DB) }

    override suspend fun getCity(name: String): CityEntity = cityDao.getDetails(name = name)

    override suspend fun addCity(name: String, latitude: Double, longitude: Double) {
        cityDao.insert(CityEntity(name, latitude, longitude))
    }

    override suspend fun removeCity(name: String) {
        cityDao.remove(name)
    }
}