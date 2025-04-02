package com.klavs.e_commerceapp.api

import com.klavs.e_commerceapp.data.model.request.CreateOrderRequest
import com.klavs.e_commerceapp.data.model.response.OrderResponse
import com.klavs.e_commerceapp.data.model.response.PagedData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OrderService {

    @GET("api/order")
    suspend fun getOrders(
        @Query("pageSize") pageSize: Int,
        @Query("firstItemIndex") firstItemIndex: Int
    ): Response<PagedData<OrderResponse>>

    @GET("api/order")
    suspend fun getOrder(@Query("orderId") orderId: Int): Response<OrderResponse>

    @POST("api/order")
    suspend fun createOrder(@Body orderRequest: CreateOrderRequest): Response<OrderResponse>

}