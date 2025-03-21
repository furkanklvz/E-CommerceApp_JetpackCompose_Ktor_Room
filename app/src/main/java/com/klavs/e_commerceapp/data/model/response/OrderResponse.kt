package com.klavs.e_commerceapp.data.model.response

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

enum class OrderStatus{
    Pending, PurchaseFailed, Approved, Delivered
}

@Serializable
data class OrderResponse(
    val addresLine: String = "",
    val city: String = "",
    val customerId: String = "",
    val deliveryFee: Double,
    val fullName: String = "",
    val orderDate: LocalDateTime,
    val orderId: Int,
    val orderItems: List<OrderItem>,
    val orderStatus: Int,
    val orderStatusValue: OrderStatus = OrderStatus.entries[orderStatus],
    val phone: String = "",
    val subTotal: Double
)