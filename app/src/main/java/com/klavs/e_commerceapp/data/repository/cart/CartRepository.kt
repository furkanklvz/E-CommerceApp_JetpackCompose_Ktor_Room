package com.klavs.e_commerceapp.data.repository.cart

import com.klavs.e_commerceapp.data.model.entity.Cart
import com.klavs.e_commerceapp.util.Resource

interface CartRepository {
    suspend fun getCart(token: String): Resource<Cart>
    suspend fun addToCart(token: String, productId: Int, quantity: Int): Resource<Cart>
    suspend fun deleteCartItem(token: String, productId: Int, quantity: Int): Resource<Cart>
}