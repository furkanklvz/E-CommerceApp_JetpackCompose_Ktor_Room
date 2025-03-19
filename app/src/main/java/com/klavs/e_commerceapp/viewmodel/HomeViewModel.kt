package com.klavs.e_commerceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klavs.e_commerceapp.data.model.entity.Product
import com.klavs.e_commerceapp.data.repository.product.ProductRepository
import com.klavs.e_commerceapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel (private val productRepo: ProductRepository): ViewModel() {
    private val _products = MutableStateFlow<Resource<List<Product>>>(Resource.Loading)
    val products = _products.asStateFlow()


    init {
        getProducts()
    }

    private fun getProducts(){
        viewModelScope.launch(Dispatchers.Main) {
            _products.value = productRepo.getProducts()
        }
    }
}