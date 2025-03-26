package com.klavs.e_commerceapp.data.repository.order

import com.klavs.e_commerceapp.data.datasource.order.OrderDatasource
import com.klavs.e_commerceapp.data.model.request.CreateOrderRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderRepositoryImpl(private val ds: OrderDatasource) : OrderRepository {
    override suspend fun getOrders(token: String, firstItemIndex: Int, pageSize: Int)
    = withContext(Dispatchers.IO) { ds.getOrders(token, firstItemIndex, pageSize) }

    override suspend fun getOrder(orderId: Int, token: String)
    = withContext(Dispatchers.IO) { ds.getOrder(orderId, token) }

    override suspend fun createOrder(request: CreateOrderRequest, token: String)
    = withContext(Dispatchers.IO) { ds.createOrder(request, token) }
}