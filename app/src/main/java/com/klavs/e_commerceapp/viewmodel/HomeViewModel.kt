package com.klavs.e_commerceapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klavs.e_commerceapp.data.model.entity.Product
import com.klavs.e_commerceapp.data.repository.product.ProductRepository
import com.klavs.e_commerceapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (private val productRepo: ProductRepository): ViewModel() {
    private val _products = MutableStateFlow<Resource<List<Product>>>(Resource.Loading)
    val products = _products.asStateFlow()


    init {
        getProducts()
    }

    private fun getProducts(){
        viewModelScope.launch {
            _products.value = productRepo.getProducts()
        }
    }
}