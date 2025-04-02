package com.klavs.e_commerceapp.helper

import com.klavs.e_commerceapp.data.model.response.ProblemDetails
import com.klavs.e_commerceapp.util.Resource
import retrofit2.Response

inline fun <reified T> parseTheResponse(response: Response<T>): Resource<T> {
    return try {
        if (response.isSuccessful) {
            runCatching {
                Resource.Success(data = response.body() as T)
            }.getOrElse {
                it.printStackTrace()
                Resource.Error(Exception("Parsing error: ${it.message}"))
            }
        } else if (response.code() == 401) {
            Resource.Unauthorized
        } else {
            Resource.Error(Exception((response.body() as ProblemDetails).title))
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Resource.Error(e)
    }
}