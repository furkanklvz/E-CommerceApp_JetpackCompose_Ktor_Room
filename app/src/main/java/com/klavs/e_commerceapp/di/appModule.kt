package com.klavs.e_commerceapp.di

import com.klavs.e_commerceapp.api.CartService
import com.klavs.e_commerceapp.api.KtorClient
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
import com.klavs.e_commerceapp.data.repository.cart.CartRepository
import com.klavs.e_commerceapp.data.repository.cart.CartRepositoryImpl
import com.klavs.e_commerceapp.data.repository.order.OrderRepository
import com.klavs.e_commerceapp.data.repository.order.OrderRepositoryImpl
import com.klavs.e_commerceapp.data.repository.product.ProductRepository
import com.klavs.e_commerceapp.data.repository.product.ProductRepositoryImpl
import com.klavs.e_commerceapp.data.repository.user.UserRepository
import com.klavs.e_commerceapp.data.repository.user.UserRepositoryImpl
import com.klavs.e_commerceapp.data.room.ECommerceDatabase
import com.klavs.e_commerceapp.viewmodel.CartViewModel
import com.klavs.e_commerceapp.viewmodel.GlobalViewModel
import com.klavs.e_commerceapp.viewmodel.HomeViewModel
import com.klavs.e_commerceapp.viewmodel.OrderViewModel
import com.klavs.e_commerceapp.viewmodel.ProductViewModel
import com.klavs.e_commerceapp.viewmodel.UserViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { ECommerceDatabase.getInstance(get()) }
    single { get<ECommerceDatabase>().tokenDao() }

    single { KtorClient.getClient() }

    singleOf(::ProductService)
    singleOf(::CartService)
    singleOf(::UserService)
    singleOf(::OrderService)

    singleOf(::ProductDatasourceImpl){bind<ProductDatasource>()}
    singleOf(::CartDatasourceImpl){bind<CartDatasource>()}
    singleOf(::UserDatasourceImpl){bind<UserDatasource>()}
    singleOf(::OrderDatasourceImpl){bind<OrderDatasource>()}

    singleOf(::ProductRepositoryImpl){bind<ProductRepository>()}
    singleOf(::CartRepositoryImpl){bind<CartRepository>()}
    singleOf(::UserRepositoryImpl){bind<UserRepository>()}
    singleOf(::OrderRepositoryImpl){bind<OrderRepository>()}

    viewModelOf(::HomeViewModel)
    viewModelOf(::ProductViewModel)
    viewModelOf(::CartViewModel)
    viewModelOf(::GlobalViewModel)
    viewModelOf(::UserViewModel)
    viewModelOf(::OrderViewModel)
}