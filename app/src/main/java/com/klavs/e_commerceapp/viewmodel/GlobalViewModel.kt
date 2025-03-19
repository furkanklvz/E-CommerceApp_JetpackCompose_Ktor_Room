package com.klavs.e_commerceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klavs.e_commerceapp.data.model.entity.Cart
import com.klavs.e_commerceapp.data.model.entity.Token
import com.klavs.e_commerceapp.data.repository.cart.CartRepository
import com.klavs.e_commerceapp.data.room.TokenDao
import com.klavs.e_commerceapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GlobalViewModel(
    private val cartRepo: CartRepository,
    private val tokenDao: TokenDao
) : ViewModel() {

    private val _cart = MutableStateFlow<Resource<Cart>>(Resource.Loading)
    val cart = _cart.asStateFlow()

    private val _token = MutableStateFlow<Token?>(null)
    val token = _token.asStateFlow()

    init {
        listenToToken()
    }

    suspend fun logout() {
        if (_token.value != null) {
            tokenDao.deleteToken(
                token = _token.value!!.value
            )
            _token.value = null
            _cart.value = Resource.Idle
        }
    }


    fun listenToToken() {
        viewModelScope.launch {
            tokenDao.getTokenFlow().collect { token ->
                _token.value = token
                if (token == null) {
                    _cart.value = Resource.Idle
                } else {
                    getCart()
                }
            }
        }
    }

    private fun getCart() {
        viewModelScope.launch(Dispatchers.Main) {
            _token.value?.let {
                _cart.value = cartRepo.getCart(it.value)
            }
        }
    }

    fun addToCart(productId: Int, quantity: Int = 1) {
        viewModelScope.launch(Dispatchers.Main) {
            _token.value?.let {
                _cart.value = cartRepo.addToCart(it.value, productId, quantity)
            }
        }
    }

    fun deleteCartItem(productId: Int, quantity: Int = 1) {
        viewModelScope.launch(Dispatchers.Main) {
            _token.value?.let {
                _cart.value = cartRepo.deleteCartItem(it.value, productId, quantity)
            }
        }
    }
}