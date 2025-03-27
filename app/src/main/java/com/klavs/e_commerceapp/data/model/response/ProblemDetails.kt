package com.klavs.e_commerceapp.data.model.response

data class ProblemDetails(
    val detail: String,
    val instance: String,
    val status: Int,
    val title: String,
    val type: String
)