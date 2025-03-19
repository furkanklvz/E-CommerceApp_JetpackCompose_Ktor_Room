package com.klavs.e_commerceapp.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class LogInRequest(
    val userName: String,
    val password: String
)
