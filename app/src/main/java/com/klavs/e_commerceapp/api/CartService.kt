package com.klavs.e_commerceapp.api

import okhttp3.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST


interface CartService {
    @GET("api/cart")
    suspend fun getCart(): Response

    @POST("api/cart")
    suspend fun addToCart(productId: Int, quantity: Int): Response

    @DELETE("api/cart")
    suspend fun deleteCartItem(productId: Int, quantity: Int): Response

}