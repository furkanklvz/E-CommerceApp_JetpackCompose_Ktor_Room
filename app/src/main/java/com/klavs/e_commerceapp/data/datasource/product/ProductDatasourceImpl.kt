package com.klavs.e_commerceapp.data.datasource.product

import com.klavs.e_commerceapp.api.ProductService
import com.klavs.e_commerceapp.data.model.entity.Product
import com.klavs.e_commerceapp.helper.parseTheResponse
import com.klavs.e_commerceapp.util.Resource

class ProductDatasourceImpl(private val service: ProductService) : ProductDatasource {
    override suspend fun getProducts(): Resource<List<Product>> {
        return try {
            val response = service.getProducts()
            parseTheResponse<List<Product>>(response)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun getProduct(id: Int): Resource<Product> {
        return try {
            val response = service.getProduct(id)
            parseTheResponse<Product>(response)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}