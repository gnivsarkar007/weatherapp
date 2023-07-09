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

    override suspend fun getCity(id: String): CityEntity = cityDao.getDetails(id = id)

    override suspend fun addCity(city: CityEntity) {
        cityDao.insert(city)
    }

    override suspend fun removeCity(id: String) {
        cityDao.remove(id)
    }
}