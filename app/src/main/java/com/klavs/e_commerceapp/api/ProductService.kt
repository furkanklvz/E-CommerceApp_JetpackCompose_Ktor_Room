package com.klavs.e_commerceapp.api

import com.klavs.e_commerceapp.data.model.entity.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {

    @GET("api/products")
    suspend fun getProducts(): Response<List<Product>>

    @GET("api/products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Response<Product>

}