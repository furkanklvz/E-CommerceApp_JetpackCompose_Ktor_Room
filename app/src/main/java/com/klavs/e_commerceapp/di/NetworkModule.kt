package com.klavs.e_commerceapp.di

import com.klavs.e_commerceapp.api.AuthInterceptor
import com.klavs.e_commerceapp.api.CartService
import com.klavs.e_commerceapp.api.OrderService
import com.klavs.e_commerceapp.api.ProductService
import com.klavs.e_commerceapp.api.UserService
import com.klavs.e_commerceapp.data.datastore.AppPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://10.0.2.2:5077"

    @Provides
    @Singleton
    fun provideAuthInterceptor(appPref: AppPref): AuthInterceptor {
        return AuthInterceptor(appPref)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCartService(retrofit: Retrofit): CartService{
        return retrofit.create(CartService::class.java)
    }

    @Provides
    @Singleton
    fun provideOrderService(retrofit: Retrofit): OrderService{
        return retrofit.create(OrderService::class.java)
    }

    @Provides
    @Singleton
    fun provideProductService(retrofit: Retrofit): ProductService{
        return retrofit.create(ProductService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService{
        return retrofit.create(UserService::class.java)
    }



}