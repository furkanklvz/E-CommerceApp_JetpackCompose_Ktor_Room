package com.klavs.e_commerceapp.data.repository.cart

import com.klavs.e_commerceapp.data.datasource.cart.CartDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor (private val ds: CartDatasource) : CartRepository {
    override suspend fun getCart() = withContext(Dispatchers.IO){ds.getCart()}
    override suspend fun addToCart(
        productId: Int,
        quantity: Int
    ) = withContext(Dispatchers.IO){ds.addToCart(productId,quantity)}

    override suspend fun deleteCartItem(
        productId: Int,
        quantity: Int
    ) = withContext(Dispatchers.IO){ds.deleteCartItem(productId,quantity)}
}