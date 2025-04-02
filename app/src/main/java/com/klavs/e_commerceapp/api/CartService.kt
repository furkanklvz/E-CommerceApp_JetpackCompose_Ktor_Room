package com.klavs.e_commerceapp.api

import com.klavs.e_commerceapp.data.model.entity.Cart
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface CartService {
    @GET("api/cart")
    suspend fun getCart(): Response<Cart>

    @POST("api/cart")
    suspend fun addToCart(
        @Query("productId") productId: Int,
        @Query("quantity") quantity: Int
    ): Response<Cart>

    @DELETE("api/cart")
    suspend fun deleteCartItem(
        @Query("productId") productId: Int,
        @Query("quantity") quantity: Int
    ): Response<Cart>

}