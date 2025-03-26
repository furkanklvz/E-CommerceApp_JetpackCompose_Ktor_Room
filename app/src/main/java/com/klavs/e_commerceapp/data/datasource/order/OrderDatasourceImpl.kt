package com.klavs.e_commerceapp.data.datasource.order

import com.klavs.e_commerceapp.api.OrderService
import com.klavs.e_commerceapp.data.model.request.CreateOrderRequest
import com.klavs.e_commerceapp.data.model.response.OrderResponse
import com.klavs.e_commerceapp.data.model.response.PagedData
import com.klavs.e_commerceapp.helper.parseTheResponse
import com.klavs.e_commerceapp.util.Resource

class OrderDatasourceImpl (private val orderService: OrderService) : OrderDatasource {
    override suspend fun getOrders(token: String, firstItemIndex: Int, pageSize: Int): Resource<PagedData<OrderResponse>> {
        return try {
            val response = orderService.getOrders(token, firstItemIndex, pageSize)
            parseTheResponse<PagedData<OrderResponse>>(response)
        }catch (e:Exception){
            Resource.Error(e)
        }
    }

    override suspend fun getOrder(orderId: Int, token: String): Resource<OrderResponse> {
        return try {
            val response = orderService.getOrder(orderId, token)
            parseTheResponse<OrderResponse>(response)
        }catch (e:Exception){
            Resource.Error(e)
        }
    }

    override suspend fun createOrder(request: CreateOrderRequest, token: String)
    : Resource<OrderResponse> {
        return try {
            val response = orderService.createOrder(request, token)
            parseTheResponse<OrderResponse>(response)
        }catch (e:Exception){
            Resource.Error(e)
        }
    }
}