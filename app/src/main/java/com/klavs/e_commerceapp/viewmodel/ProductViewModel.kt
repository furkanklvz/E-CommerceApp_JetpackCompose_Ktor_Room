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

class ProductViewModel (private val productRepo: ProductRepository): ViewModel() {

    private val _product = MutableStateFlow<Resource<Product>>(Resource.Idle)
    val product = _product.asStateFlow()

    fun getProduct(id: Int){
        _product.value = Resource.Loading
        viewModelScope.launch(Dispatchers.Main) {
            _product.value = productRepo.getProduct(id)
        }
    }

}