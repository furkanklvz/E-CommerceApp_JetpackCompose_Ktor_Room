package com.klavs.e_commerceapp.data.datasource.cart

import com.klavs.e_commerceapp.data.model.entity.Cart
import com.klavs.e_commerceapp.util.Resource

interface CartDatasource {
    suspend fun getCart(): Resource<Cart>
    suspend fun addToCart(productId: Int, quantity: Int): Resource<Cart>
    suspend fun deleteCartItem(productId: Int, quantity: Int): Resource<Cart>
}