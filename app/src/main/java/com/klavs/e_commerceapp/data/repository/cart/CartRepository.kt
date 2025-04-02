package com.klavs.e_commerceapp.data.repository.cart

import com.klavs.e_commerceapp.data.model.entity.Cart
import com.klavs.e_commerceapp.util.Resource

interface CartRepository {
    suspend fun getCart(): Resource<Cart>
    suspend fun addToCart(productId: Int, quantity: Int): Resource<Cart>
    suspend fun deleteCartItem(productId: Int, quantity: Int): Resource<Cart>
}