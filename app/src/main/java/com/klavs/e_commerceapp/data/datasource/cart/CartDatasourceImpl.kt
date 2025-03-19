package com.klavs.e_commerceapp.data.datasource.cart

import com.klavs.e_commerceapp.api.CartService
import com.klavs.e_commerceapp.data.model.entity.Cart
import com.klavs.e_commerceapp.helper.parseTheResponse
import com.klavs.e_commerceapp.util.Resource
import io.ktor.http.HttpStatusCode

class CartDatasourceImpl(private val cartService: CartService) : CartDatasource {
    override suspend fun getCart(token: String): Resource<Cart> {
        return try {
            val response = cartService.getCart(token)
            parseTheResponse<Cart>(response)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun addToCart(
        token: String,
        productId: Int,
        quantity: Int
    ): Resource<Cart> {
        return try {
            val response = cartService.addToCart(token, productId, quantity)
            parseTheResponse<Cart>(response)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun deleteCartItem(
        token: String,
        productId: Int,
        quantity: Int
    ): Resource<Cart> {
        return try {
            val response = cartService.deleteCartItem(token, productId, quantity)
            parseTheResponse<Cart>(response)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}