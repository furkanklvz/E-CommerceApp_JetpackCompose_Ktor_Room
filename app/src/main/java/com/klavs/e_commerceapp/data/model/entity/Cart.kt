package com.klavs.e_commerceapp.data.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class Cart(
    val cartId: Int,
    val cartItems: List<CartItem> = emptyList(),
    val customerId: String
)