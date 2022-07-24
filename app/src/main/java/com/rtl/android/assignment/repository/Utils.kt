package com.rtl.android.assignment.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> Response<T>
): Result<T> {
    return withContext(dispatcher) {
        try {
            val response = apiCall.invoke()
            val body = response.body()
            if (response.isSuccessful) {
                Result.Success(body!!)
            } else {
                Result.Error(type = Result.Error.Type.API)
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> Result.Error(type = Result.Error.Type.NETWORK)
                is HttpException -> {
                    Result.Error(type = Result.Error.Type.API)
                }
                else -> {
                    Result.Error(type = Result.Error.Type.NETWORK)
                }
            }
        }
    }
}
