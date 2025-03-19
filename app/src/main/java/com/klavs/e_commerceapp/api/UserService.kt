package com.klavs.e_commerceapp.api

import com.klavs.e_commerceapp.data.model.request.LogInRequest
import com.klavs.e_commerceapp.data.model.request.RegisterRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path

class UserService (private val client: HttpClient) {
    private val baseUrl = "10.0.2.2"

    suspend fun logIn(request: LogInRequest): HttpResponse
    {
        val response = client.post {
            url {
                protocol = URLProtocol.HTTP
                host = baseUrl
                port = 5077
                path("api", "account", "login")
            }
            contentType(ContentType.Application.Json)
            setBody(body = request)
        }
        return response
    }

    suspend fun register(request: RegisterRequest): HttpResponse
    {
        val response = client.post {
            url {
                protocol = URLProtocol.HTTP
                host = baseUrl
                port = 5077
                path("api", "account", "register")
            }
            contentType(ContentType.Application.Json)
            setBody(body = request)
        }
        return response
    }

}