package com.klavs.e_commerceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klavs.e_commerceapp.data.model.response.OrderResponse
import com.klavs.e_commerceapp.data.repository.order.OrderRepository
import com.klavs.e_commerceapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel (private val orderRepo: OrderRepository): ViewModel() {

    private val _ordersResource= MutableStateFlow<Resource<List<OrderResponse>>>(Resource.Loading)
    val ordersResource = _ordersResource.asStateFlow()

    fun getOrders(token: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _ordersResource.value = orderRepo.getOrders(token)
        }
    }

}