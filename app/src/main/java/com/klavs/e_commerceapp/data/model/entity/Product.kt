package com.klavs.e_commerceapp.data.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val productId: Int,
    val description: String = "",
    val imageUrl: String? = null,
    val isActive: Boolean,
    val name: String = "",
    val price: Double,
    val stock: Int,
    val rating: Float? = null,
    val raters: Int = 0
)