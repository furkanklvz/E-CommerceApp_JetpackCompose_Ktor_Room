package com.klavs.e_commerceapp.api

import com.klavs.e_commerceapp.data.model.request.LogInRequest
import com.klavs.e_commerceapp.data.model.request.RegisterRequest
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("api/account/login")
    suspend fun logIn(@Body request: LogInRequest): Response

    @POST("api/account/register")
    suspend fun register(@Body request: RegisterRequest): Response

}