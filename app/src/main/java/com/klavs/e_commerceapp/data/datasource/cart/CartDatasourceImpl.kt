package com.klavs.e_commerceapp.data.datasource.cart

import com.klavs.e_commerceapp.api.CartService
import com.klavs.e_commerceapp.data.model.entity.Cart
import com.klavs.e_commerceapp.helper.parseTheResponse
import com.klavs.e_commerceapp.util.Resource
import javax.inject.Inject

class CartDatasourceImpl @Inject constructor(private val cartService: CartService) : CartDatasource {
    override suspend fun getCart(): Resource<Cart> {
        return try {
            val response = cartService.getCart()
            return parseTheResponse<Cart>(response)
        }catch (e: Exception){
            Resource.Error(e)
        }
    }

    override suspend fun addToCart(
        productId: Int,
        quantity: Int
    ): Resource<Cart> {
        return try {
            val response = cartService.getCart()
            return parseTheResponse<Cart>(response)
        }catch (e: Exception){
            Resource.Error(e)
        }
    }

    override suspend fun deleteCartItem(
        productId: Int,
        quantity: Int
    ): Resource<Cart> {
        return try {
            val response = cartService.getCart()
            return parseTheResponse<Cart>(response)
        }catch (e: Exception){
            Resource.Error(e)
        }
    }
}