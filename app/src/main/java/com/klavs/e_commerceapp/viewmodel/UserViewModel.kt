package com.klavs.e_commerceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klavs.e_commerceapp.data.model.request.LogInRequest
import com.klavs.e_commerceapp.data.model.request.RegisterRequest
import com.klavs.e_commerceapp.data.model.response.LogInResponse
import com.klavs.e_commerceapp.data.repository.user.UserRepository
import com.klavs.e_commerceapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val userRepo: UserRepository) : ViewModel() {

    private val _logInResource = MutableStateFlow<Resource<LogInResponse>>(Resource.Idle)
    val logInResource = _logInResource.asStateFlow()

    private val _registerResource = MutableStateFlow<Resource<LogInResponse>>(Resource.Idle)
    val registerResource = _registerResource.asStateFlow()

    fun logIn(request: LogInRequest) {
        _logInResource.value = Resource.Loading
        viewModelScope.launch(Dispatchers.Main) {
            _logInResource.value = userRepo.logIn(request)
        }
    }

    fun register(request: RegisterRequest) {
        _registerResource.value = Resource.Loading
        viewModelScope.launch(Dispatchers.Main) {
            _registerResource.value = userRepo.register(request).also {
                if (it is Resource.Error){
                    it.throwable.printStackTrace()
                }
            }
        }
    }


}