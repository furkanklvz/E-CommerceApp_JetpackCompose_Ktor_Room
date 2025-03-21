package com.klavs.e_commerceapp.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(
    val orderId: Int,
    val orderItemId: Int,
    val price: Double,
    val productId: Int,
    val productImageUrl: String? = null,
    val productName: String? = null,
    val quantity: Int
)