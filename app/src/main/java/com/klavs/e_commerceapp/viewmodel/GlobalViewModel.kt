package com.klavs.e_commerceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klavs.e_commerceapp.data.model.entity.Cart
import com.klavs.e_commerceapp.data.model.entity.Account
import com.klavs.e_commerceapp.data.repository.cart.CartRepository
import com.klavs.e_commerceapp.data.room.AccountDao
import com.klavs.e_commerceapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GlobalViewModel(
    private val cartRepo: CartRepository,
    private val accountDao: AccountDao
) : ViewModel() {

    private val _cart = MutableStateFlow<Resource<Cart>>(Resource.Loading)
    val cart = _cart.asStateFlow()

    private val _account = MutableStateFlow<Account?>(null)
    val account = _account.asStateFlow()

    init {
        listenToAccount()
    }

    suspend fun logout() {
        if (_account.value != null) {
            accountDao.deleteAccount(
                token = _account.value!!.token
            )
            _account.value = null
            _cart.value = Resource.Idle
        }
    }


    fun listenToAccount() {
        viewModelScope.launch {
            accountDao.getAccountFlow().collect { account ->
                _account.value = account
                if (account == null) {
                    _cart.value = Resource.Idle
                } else {
                    getCart()
                }
            }
        }
    }

    fun getCart() {
        viewModelScope.launch(Dispatchers.Main) {
            _account.value?.let {
                _cart.value = cartRepo.getCart(it.token).also { resource->
                    if (resource.isUnauthorized()){
                        logout()
                    }
                }
            }
        }
    }

    fun addToCart(productId: Int, quantity: Int = 1) {
        viewModelScope.launch(Dispatchers.Main) {
            _account.value?.let {
                _cart.value = cartRepo.addToCart(it.token, productId, quantity)
            }
        }
    }

    fun deleteCartItem(productId: Int, quantity: Int = 1) {
        viewModelScope.launch(Dispatchers.Main) {
            _account.value?.let {
                _cart.value = cartRepo.deleteCartItem(it.token, productId, quantity)
            }
        }
    }
}