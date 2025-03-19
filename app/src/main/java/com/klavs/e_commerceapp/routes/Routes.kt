package com.klavs.e_commerceapp.routes

import kotlinx.serialization.Serializable

@Serializable
data object _Home

@Serializable
data object _Search

@Serializable
data object _ShoppingCart

@Serializable
data object _Profile

@Serializable
data class ProductDetails(val id: Int)

@Serializable
data object ShoppingCartTop

@Serializable
data object LogIn

@Serializable
data object Register