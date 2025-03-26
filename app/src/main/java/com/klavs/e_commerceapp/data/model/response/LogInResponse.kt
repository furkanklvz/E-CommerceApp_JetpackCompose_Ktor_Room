package com.klavs.e_commerceapp.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LogInResponse(
    val token: String = "",
    val fullName: String = "",
    val userName: String = "",
    val email: String = ""
)
