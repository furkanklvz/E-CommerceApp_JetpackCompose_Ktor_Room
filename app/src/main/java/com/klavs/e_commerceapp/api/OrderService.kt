package com.klavs.e_commerceapp.api

import com.klavs.e_commerceapp.data.model.request.CreateOrderRequest
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OrderService {
    @GET("api/order")
    suspend fun getOrders(firstItemIndex: Int, pageSize: Int): Response

    @GET("api/order")
    suspend fun getOrder(orderId: Int): Response

    @POST("api/order")
    suspend fun createOrder(@Body request: CreateOrderRequest): Response

}