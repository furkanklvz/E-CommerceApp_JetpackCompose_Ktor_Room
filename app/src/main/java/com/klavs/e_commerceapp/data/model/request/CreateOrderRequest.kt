package com.klavs.e_commerceapp.data.model.request

import kotlinx.serialization.Serializable


@Serializable
data class CreateOrderRequest(
    val addresLine: String,
    val city: String,
    val name: String,
    val surname: String,
    val phone: String,
    val email: String,
    val cardHolderName: String,
    val cardNumber: String,
    val cardExpireMonth: String,
    val cardExpireYear: String,
    val cardCvc: String

)