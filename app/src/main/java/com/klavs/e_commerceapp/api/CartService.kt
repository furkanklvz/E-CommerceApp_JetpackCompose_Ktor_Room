package com.klavs.e_commerceapp.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.timeout
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.URLProtocol
import io.ktor.http.path

class CartService(private val client: HttpClient) {
    private val baseUrl = "10.0.2.2"

    suspend fun getCart(token: String): HttpResponse {
        val response = client.get {
            url {
                protocol = URLProtocol.HTTP
                host = baseUrl
                port = 5077
                path("api", "cart")
            }
            header("Authorization", "Bearer $token")
            timeout {
                requestTimeoutMillis = 15000
            }
        }
        return response
    }

    suspend fun addToCart(token: String, productId: Int, quantity: Int): HttpResponse {
        val response = client.post {
            url {
                protocol = URLProtocol.HTTP
                host = baseUrl
                port = 5077
                path("api", "cart")
                parameter("productId", productId)
                parameter("quantity", quantity)
            }
            header("Authorization", "Bearer $token")
            timeout {
                requestTimeoutMillis = 15000
            }
        }
        return response
    }

    suspend fun deleteCartItem(token: String, productId: Int, quantity: Int): HttpResponse {
        val response = client.delete {
            url {
                protocol = URLProtocol.HTTP
                host = baseUrl
                port = 5077
                path("api", "cart")
                parameter("productId", productId)
                parameter("quantity", quantity)
            }
            header("Authorization", "Bearer $token")
            timeout {
                requestTimeoutMillis = 15000
            }
        }
        return response
    }
}