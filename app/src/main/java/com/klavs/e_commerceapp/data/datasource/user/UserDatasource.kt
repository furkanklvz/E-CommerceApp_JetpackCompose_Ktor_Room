package com.klavs.e_commerceapp.data.datasource.user

import com.klavs.e_commerceapp.data.model.request.LogInRequest
import com.klavs.e_commerceapp.data.model.request.RegisterRequest
import com.klavs.e_commerceapp.data.model.response.LogInResponse
import com.klavs.e_commerceapp.util.Resource

interface UserDatasource {
    suspend fun logIn(request: LogInRequest): Resource<LogInResponse>
    suspend fun register(request: RegisterRequest): Resource<LogInResponse>
}