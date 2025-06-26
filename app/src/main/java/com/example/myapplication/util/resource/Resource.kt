package com.example.myapplication.util.resource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class Resource<T> (
    val data:T?=null,
    val message:String?=null
) {
    class Success<T>(data:T):Resource<T>(data)
    class Error<T>(message: String,data:T?=null):Resource<T>(data,message)
    class Loading<T> : Resource<T>()
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
    return withContext(Dispatchers.IO) {
        try {
            val response = apiCall()
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }
}