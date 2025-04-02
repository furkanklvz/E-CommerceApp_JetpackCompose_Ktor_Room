package com.klavs.e_commerceapp.data.repository.product

import com.klavs.e_commerceapp.data.datasource.product.ProductDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor (private val ds: ProductDatasource) : ProductRepository {
    override suspend fun getProducts() = withContext(Dispatchers.IO){ds.getProducts()}
    override suspend fun getProduct(id: Int) = withContext(Dispatchers.IO){ ds.getProduct(id)}
}