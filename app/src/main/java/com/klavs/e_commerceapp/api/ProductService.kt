package com.klavs.e_commerceapp.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.URLProtocol
import io.ktor.http.path

class ProductService(private val client: HttpClient) {
    private val baseUrl = "10.0.2.2"

    suspend fun getProducts(): HttpResponse {
        val response = client.get {
            url {
                protocol = URLProtocol.HTTP
                host = baseUrl
                port = 5077
                path("api","products")
            }
            timeout {
                requestTimeoutMillis = 15000
            }
        }
        return response
    }

    suspend fun getProduct(id: Int): HttpResponse {
        val response = client.get {
            url {
                protocol = URLProtocol.HTTP
                host = baseUrl
                port = 5077
                path("api","products", "$id")
            }
            timeout {
                requestTimeoutMillis = 15000
            }
        }
        return response
    }
}