package com.klavs.e_commerceapp.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val userName: String,
    val password: String,
    val fullName: String? = null,
    val email: String
)
