package com.klavs.e_commerceapp.data.model.request

data class CreateOrderRequest(
    val addresLine: String,
    val city: String,
    val fullName: String,
    val phone: String
)