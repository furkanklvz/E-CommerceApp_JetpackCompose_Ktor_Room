package com.klavs.e_commerceapp.data.repository.user

import com.klavs.e_commerceapp.data.datasource.user.UserDatasource
import com.klavs.e_commerceapp.data.model.request.LogInRequest
import com.klavs.e_commerceapp.data.model.request.RegisterRequest
import com.klavs.e_commerceapp.data.model.response.LogInResponse
import com.klavs.e_commerceapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepositoryImpl(private val ds: UserDatasource) : UserRepository {
    override suspend fun logIn(request: LogInRequest) =
        withContext(Dispatchers.IO) { ds.logIn(request) }

    override suspend fun register(request: RegisterRequest) =
        withContext(Dispatchers.IO) { ds.register(request) }
}