package com.klavs.e_commerceapp.api

import com.klavs.e_commerceapp.data.model.request.LogInRequest
import com.klavs.e_commerceapp.data.model.request.RegisterRequest
import com.klavs.e_commerceapp.data.model.response.LogInResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("api/account/login")
    suspend fun logIn(@Body request: LogInRequest): Response<LogInResponse>

    @POST("api/account/register")
    suspend fun register(@Body request: RegisterRequest): Response<LogInResponse>

}