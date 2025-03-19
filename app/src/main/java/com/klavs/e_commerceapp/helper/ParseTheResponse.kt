package com.klavs.e_commerceapp.helper

import com.klavs.e_commerceapp.util.Resource
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

suspend inline fun <reified T> parseTheResponse(response: HttpResponse): Resource<T> {
    return try {
        if (response.status.isSuccess()) {
            runCatching {
                Resource.Success(data = response.body<T>())
            }.getOrElse {
                Resource.Error(Exception("Parsing error: ${it.message}"))
            }
        } else {
            Resource.Error(Exception(response.body<String>()))
        }
    } catch (e: Exception) {
        Resource.Error(e)
    }
}