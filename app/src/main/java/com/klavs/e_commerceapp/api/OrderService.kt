package com.klavs.e_commerceapp.api

import com.klavs.e_commerceapp.data.model.request.CreateOrderRequest
import io.ktor.client.HttpClient
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.URLProtocol
import io.ktor.http.path

class OrderService(private val client: HttpClient) {
    private val baseUrl = "10.0.2.2"
    suspend fun getOrders(token: String): HttpResponse {
        val response = client.get {
            url {
                protocol = URLProtocol.HTTP
                host = baseUrl
                port = 5077
                path("api", "order")
            }
            header("Authorization", "Bearer $token")
            timeout {
                requestTimeoutMillis = 15000
            }
        }
        return response
    }

    suspend fun getOrder(orderId: Int, token: String): HttpResponse {
        val response = client.get {
            url {
                protocol = URLProtocol.HTTP
                host = baseUrl
                port = 5077
                path("api", "order", "$orderId")
            }
            header("Authorization", "Bearer $token")
            timeout {
                requestTimeoutMillis = 15000
            }
        }
        return response
    }
    suspend fun createOrder(request: CreateOrderRequest, token: String): HttpResponse {
        val response = client.get {
            url {
                protocol = URLProtocol.HTTP
                host = baseUrl
                port = 5077
                path("api", "order")
            }
            headers {
                append("Authorization", "Bearer $token")
                append("Content-Type", "application/json")
            }
            setBody(request)
        }
        return response
    }
}