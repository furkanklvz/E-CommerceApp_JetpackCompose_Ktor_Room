package com.klavs.e_commerceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klavs.e_commerceapp.data.model.request.CreateOrderRequest
import com.klavs.e_commerceapp.data.model.response.OrderResponse
import com.klavs.e_commerceapp.data.repository.order.OrderRepository
import com.klavs.e_commerceapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val orderRepo: OrderRepository) : ViewModel() {

    private val _orderResource = MutableStateFlow<Resource<OrderResponse>>(Resource.Idle)
    val orderResource = _orderResource.asStateFlow()

    fun createOrder(request: CreateOrderRequest){
        viewModelScope.launch (Dispatchers.Main) {
            _orderResource.value = orderRepo.createOrder(
                request = request
            )
        }
    }

}