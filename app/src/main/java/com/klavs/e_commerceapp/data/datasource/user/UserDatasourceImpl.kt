package com.klavs.e_commerceapp.data.datasource.user

import com.klavs.e_commerceapp.api.UserService
import com.klavs.e_commerceapp.data.model.entity.Account
import com.klavs.e_commerceapp.data.model.request.LogInRequest
import com.klavs.e_commerceapp.data.model.request.RegisterRequest
import com.klavs.e_commerceapp.data.model.response.LogInResponse
import com.klavs.e_commerceapp.data.room.AccountDao
import com.klavs.e_commerceapp.helper.parseTheResponse
import com.klavs.e_commerceapp.util.Resource

class UserDatasourceImpl(
    private val userService: UserService,
    private val accountDao: AccountDao
) : UserDatasource {
    override suspend fun logIn(request: LogInRequest): Resource<LogInResponse> {
        return try {
            val response = userService.logIn(request)
            parseTheResponse<LogInResponse>(response).also { logInResponseResource ->
                if (logInResponseResource is Resource.Success) {
                    accountDao.updateAccount(
                        Account(
                            token = logInResponseResource.data.token,
                            createdAt = System.currentTimeMillis(),
                            fullName = logInResponseResource.data.fullName,
                            userName = logInResponseResource.data.userName,
                            email = logInResponseResource.data.email
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun register(request: RegisterRequest): Resource<LogInResponse> {
        return try {
            val response = userService.register(request)
            parseTheResponse<LogInResponse>(response).also { logInResponseResource ->
                if (logInResponseResource is Resource.Success) {
                    accountDao.updateAccount(
                        Account(
                            token = logInResponseResource.data.token,
                            createdAt = System.currentTimeMillis(),
                            fullName = logInResponseResource.data.fullName,
                            userName = logInResponseResource.data.userName,
                            email = logInResponseResource.data.email
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}