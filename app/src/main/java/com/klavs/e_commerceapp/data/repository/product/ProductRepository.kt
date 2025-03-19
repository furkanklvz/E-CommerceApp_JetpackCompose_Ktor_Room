package com.klavs.e_commerceapp.data.repository.product

import com.klavs.e_commerceapp.data.model.entity.Product
import com.klavs.e_commerceapp.util.Resource

interface ProductRepository {
    suspend fun getProducts(): Resource<List<Product>>
    suspend fun getProduct(id: Int): Resource<Product>
}