package com.klavs.e_commerceapp.routes

import kotlinx.serialization.Serializable

@Serializable
data object _Home

@Serializable
data object _Search

@Serializable
data object _ShoppingCart

@Serializable
data object _Menu

@Serializable
data class ProductDetails(val id: Int)

@Serializable
data object ShoppingCartTop

@Serializable
data object LogIn

@Serializable
data object Register

@Serializable
data object Orders

@Serializable
data class CreateOrder (val cart: String)

@Serializable
data object Account