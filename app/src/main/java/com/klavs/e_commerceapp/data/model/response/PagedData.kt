package com.klavs.e_commerceapp.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PagedData<out T>(
    val pageSize : Int,
    val lastItemIndex : Int,
    val totalRecords: Int,
    val totalPages: Int,
    val data : List<T> = emptyList()
)
