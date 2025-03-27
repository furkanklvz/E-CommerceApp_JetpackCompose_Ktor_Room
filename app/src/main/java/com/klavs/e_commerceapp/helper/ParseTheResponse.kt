package com.klavs.e_commerceapp.helper

import android.util.Log
import com.klavs.e_commerceapp.data.model.response.ProblemDetails
import com.klavs.e_commerceapp.util.Resource
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

suspend inline fun <reified T> parseTheResponse(response: HttpResponse): Resource<T> {
    return try {
        if (response.status.isSuccess()) {
            Log.e("bodyAsText", response.bodyAsText())
            runCatching {
                Resource.Success(data = response.body<T>())
            }.getOrElse {
                it.printStackTrace()
                Resource.Error(Exception("Parsing error: ${it.message}"))
            }
        } else if (response.status == HttpStatusCode.Unauthorized) {
            Resource.Unauthorized
        } else {
            Resource.Error(Exception(response.body<ProblemDetails>().title))
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Resource.Error(e)
    }
}