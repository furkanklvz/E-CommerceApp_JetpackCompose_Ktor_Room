package com.klavs.e_commerceapp.di

import android.content.Context
import androidx.room.RoomDatabase
import com.klavs.e_commerceapp.api.CartService
import com.klavs.e_commerceapp.api.OrderService
import com.klavs.e_commerceapp.api.ProductService
import com.klavs.e_commerceapp.api.UserService
import com.klavs.e_commerceapp.data.datasource.cart.CartDatasource
import com.klavs.e_commerceapp.data.datasource.cart.CartDatasourceImpl
import com.klavs.e_commerceapp.data.datasource.order.OrderDatasource
import com.klavs.e_commerceapp.data.datasource.order.OrderDatasourceImpl
import com.klavs.e_commerceapp.data.datasource.product.ProductDatasource
import com.klavs.e_commerceapp.data.datasource.product.ProductDatasourceImpl
import com.klavs.e_commerceapp.data.datasource.user.UserDatasource
import com.klavs.e_commerceapp.data.datasource.user.UserDatasourceImpl
import com.klavs.e_commerceapp.data.datastore.AppPref
import com.klavs.e_commerceapp.data.repository.cart.CartRepository
import com.klavs.e_commerceapp.data.repository.cart.CartRepositoryImpl
import com.klavs.e_commerceapp.data.repository.order.OrderRepository
import com.klavs.e_commerceapp.data.repository.order.OrderRepositoryImpl
import com.klavs.e_commerceapp.data.repository.product.ProductRepository
import com.klavs.e_commerceapp.data.repository.product.ProductRepositoryImpl
import com.klavs.e_commerceapp.data.repository.user.UserRepository
import com.klavs.e_commerceapp.data.repository.user.UserRepositoryImpl
import com.klavs.e_commerceapp.data.room.AccountDao
import com.klavs.e_commerceapp.data.room.ECommerceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppPref(@ApplicationContext context: Context): AppPref {
        return AppPref(context)
    }

    @Provides
    @Singleton
    fun provideCartDatasource(cartService: CartService): CartDatasource = CartDatasourceImpl(
        cartService = cartService
    )

    @Provides
    @Singleton
    fun provideCartRepository(cartDatasource: CartDatasource): CartRepository =
        CartRepositoryImpl(cartDatasource)

    @Provides
    @Singleton
    fun provideOrderDatasource(orderService: OrderService): OrderDatasource =
        OrderDatasourceImpl(orderService)

    @Provides
    @Singleton
    fun provideOrderRepository(orderDatasource: OrderDatasource): OrderRepository =
        OrderRepositoryImpl(orderDatasource)

    @Provides
    @Singleton
    fun provideProductDatasource(productService: ProductService): ProductDatasource =
        ProductDatasourceImpl(productService)

    @Provides
    @Singleton
    fun provideProductRepository(productDatasource: ProductDatasource): ProductRepository =
        ProductRepositoryImpl(productDatasource)

    @Provides
    @Singleton
    fun provideECommerceDatabase(@ApplicationContext context: Context): ECommerceDatabase =
        ECommerceDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideAccountDao(eCommerceDatabase: ECommerceDatabase): AccountDao =
        eCommerceDatabase.accountDao()

    @Provides
    @Singleton
    fun provideUserDatasource(userService: UserService, accountDao: AccountDao): UserDatasource =
        UserDatasourceImpl(userService, accountDao)

    @Provides
    @Singleton
    fun provideUserRepository(userDatasource: UserDatasource): UserRepository =
        UserRepositoryImpl(userDatasource)

}