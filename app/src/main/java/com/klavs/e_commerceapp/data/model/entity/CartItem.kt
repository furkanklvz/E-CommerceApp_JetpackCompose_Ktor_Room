package com.klavs.e_commerceapp.data.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val productId: Int,
    val productImageUrl: String? = null,
    val productName: String = "",
    val productPrice: Double,
    val quantity: Int
)