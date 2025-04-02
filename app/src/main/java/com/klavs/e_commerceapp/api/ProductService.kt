package com.klavs.e_commerceapp.api

import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {

    @GET("api/products")
    suspend fun getProducts(): Response

    @GET("api/products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Response

}