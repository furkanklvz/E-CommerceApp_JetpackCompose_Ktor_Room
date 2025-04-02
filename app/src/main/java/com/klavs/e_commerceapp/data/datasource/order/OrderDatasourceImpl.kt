package com.klavs.e_commerceapp.data.datasource.order

import com.klavs.e_commerceapp.api.OrderService
import com.klavs.e_commerceapp.data.model.request.CreateOrderRequest
import com.klavs.e_commerceapp.data.model.response.OrderResponse
import com.klavs.e_commerceapp.data.model.response.PagedData
import com.klavs.e_commerceapp.helper.parseTheResponse
import com.klavs.e_commerceapp.util.Resource
import javax.inject.Inject

class OrderDatasourceImpl @Inject constructor (private val orderService: OrderService) : OrderDatasource {
    override suspend fun getOrders(firstItemIndex: Int, pageSize: Int): Resource<PagedData<OrderResponse>> {
        return try {
            val response = orderService.getOrders(firstItemIndex, pageSize)
            parseTheResponse<PagedData<OrderResponse>>(response)
        }catch (e:Exception){
            Resource.Error(e)
        }
    }

    override suspend fun getOrder(orderId: Int): Resource<OrderResponse> {
        return try {
            val response = orderService.getOrder(orderId)
            parseTheResponse<OrderResponse>(response)
        }catch (e:Exception){
            Resource.Error(e)
        }
    }

    override suspend fun createOrder(request: CreateOrderRequest)
    : Resource<OrderResponse> {
        return try {
            val response = orderService.createOrder(request)
            parseTheResponse<OrderResponse>(response)
        }catch (e:Exception){
            Resource.Error(e)
        }
    }
}