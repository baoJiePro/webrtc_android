package com.baojie.network.retrofit.intercept

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class TimeoutInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var realChain = chain
        request.header("connect_timeout")?.let {
            realChain = realChain.withConnectTimeout(it.toInt(), TimeUnit.SECONDS)
        }
        request.header("read_timeout")?.let {
            realChain = realChain.withReadTimeout(it.toInt(), TimeUnit.SECONDS)
        }
        request.header("write_timeout")?.let {
            realChain = realChain.withWriteTimeout(it.toInt(), TimeUnit.SECONDS)
        }
//        log(
//            "connect_timeout:${realChain.connectTimeoutMillis()}, " +
//                    "read_timeout :${realChain.readTimeoutMillis()},write_timeout : ${realChain.writeTimeoutMillis()}"
//        )
        return realChain.proceed(request)

    }

}