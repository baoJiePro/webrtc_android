package com.baojie.network.retrofit.intercept

import com.blankj.utilcode.util.LogUtils
import okhttp3.Interceptor
import okhttp3.Response


interface TokenProvider {
    fun getToken(): String?
}

class AuthInterceptor(private val tokenProvider: TokenProvider) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val userRequest = chain.request()
        val requestBuilder = userRequest.newBuilder()
        val token = tokenProvider.getToken()
        token?.let {
            requestBuilder.header("qd-token", token)
        }
        LogUtils.d("kbtest token = $token")
        return chain.proceed(requestBuilder.build())

    }
}