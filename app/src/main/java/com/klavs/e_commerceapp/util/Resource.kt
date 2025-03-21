package com.klavs.e_commerceapp.util

sealed class Resource<out T> {
    data class Success<out T>(val data: T): Resource<T>()
    data class Error(val throwable: Throwable): Resource<Nothing>()
    data object Unauthorized : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
    data object Idle : Resource<Nothing>()

    fun isSuccess() = this is Success
    fun isLoading() = this is Loading
    fun isError() = this is Error
    fun isIdle() = this is Idle
    fun isUnauthorized() = this is Unauthorized
}