package com.klavs.e_commerceapp.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.klavs.e_commerceapp.api.AuthInterceptor
import com.klavs.e_commerceapp.api.CartService
import com.klavs.e_commerceapp.api.OrderService
import com.klavs.e_commerceapp.api.ProductService
import com.klavs.e_commerceapp.api.UserService
import com.klavs.e_commerceapp.data.datastore.AppPref
import com.klavs.e_commerceapp.data.room.AccountDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://10.0.2.2:5077"

    @Provides
    @Singleton
    fun provideAuthInterceptor(appPref: AppPref, accountDao: AccountDao): AuthInterceptor {
        return AuthInterceptor(appPref, accountDao)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // İstek ve yanıt detaylarını loglar
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
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