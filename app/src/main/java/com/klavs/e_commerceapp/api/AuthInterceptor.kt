package com.klavs.e_commerceapp.api

import com.klavs.e_commerceapp.data.datastore.AppPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val appPref: AppPref) : Interceptor {

    var token: String? = null

    init {
        CoroutineScope(Dispatchers.IO).launch{
            appPref.getToken().collect {
                token = it[AppPref.TOKEN]
            }
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")
        token?.let { requestBuilder.addHeader("Authorization", "Bearer $it") }

        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}