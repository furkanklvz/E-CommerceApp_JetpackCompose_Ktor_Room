package com.klavs.e_commerceapp.data.datasource.order

import com.klavs.e_commerceapp.data.model.request.CreateOrderRequest
import com.klavs.e_commerceapp.data.model.response.OrderResponse
import com.klavs.e_commerceapp.util.Resource

interface OrderDatasource {
    suspend fun getOrders(token: String): Resource<List<OrderResponse>>
    suspend fun getOrder(orderId: Int, token: String): Resource<OrderResponse>
    suspend fun createOrder(request: CreateOrderRequest, token: String): Resource<OrderResponse>
}