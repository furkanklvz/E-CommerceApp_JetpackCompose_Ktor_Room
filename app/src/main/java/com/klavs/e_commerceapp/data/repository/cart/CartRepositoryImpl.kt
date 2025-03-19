package com.klavs.e_commerceapp.data.repository.cart

import com.klavs.e_commerceapp.data.datasource.cart.CartDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepositoryImpl (private val ds: CartDatasource) : CartRepository {
    override suspend fun getCart(token: String) = withContext(Dispatchers.IO){ds.getCart(token)}
    override suspend fun addToCart(
        token: String,
        productId: Int,
        quantity: Int
    ) = withContext(Dispatchers.IO){ds.addToCart(token,productId,quantity)}

    override suspend fun deleteCartItem(
        token: String,
        productId: Int,
        quantity: Int
    ) = withContext(Dispatchers.IO){ds.deleteCartItem(token,productId,quantity)}
}