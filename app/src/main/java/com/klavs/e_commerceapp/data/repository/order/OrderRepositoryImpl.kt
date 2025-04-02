package com.klavs.e_commerceapp.data.repository.order

import com.klavs.e_commerceapp.data.datasource.order.OrderDatasource
import com.klavs.e_commerceapp.data.model.request.CreateOrderRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(private val ds: OrderDatasource) : OrderRepository {
    override suspend fun getOrders(firstItemIndex: Int, pageSize: Int)
    = withContext(Dispatchers.IO) { ds.getOrders(firstItemIndex, pageSize) }

    override suspend fun getOrder(orderId: Int)
    = withContext(Dispatchers.IO) { ds.getOrder(orderId) }

    override suspend fun createOrder(request: CreateOrderRequest)
    = withContext(Dispatchers.IO) { ds.createOrder(request) }
}