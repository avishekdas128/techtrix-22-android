package com.orangeink.techtrix.util

import retrofit2.Response

/**
 * Abstract Base Data source class with error handling
 */
abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                if (response.code() == 204)
                    return Resource.Empty(response.code())
                val body = response.body()
                if (body != null) return Resource.Success(body)
            }
            return error("Something went wrong!")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        return Resource.Error(message)
    }
}