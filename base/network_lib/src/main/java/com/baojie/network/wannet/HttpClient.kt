package com.baojie.network.wannet

import com.baojie.network.retrofit.intercept.*
import com.blankj.utilcode.util.Utils
import me.hgj.jetpackmvvm.network.interceptor.logging.LogInterceptor
import okhttp3.Cache

import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit


object HttpClient {


    const val MAX_REQUESTS = 4
    val client: OkHttpClient by lazy {
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = MAX_REQUESTS
        OkHttpClient.Builder()
            .dispatcher(dispatcher)
            //设置缓存配置 缓存最大10M
            .cache(Cache(File(Utils.getApp().cacheDir, "cxk_cache"), 10 * 1024 * 1024))
            .addInterceptor(TimeoutInterceptor())
            .addInterceptor(MyHeadInterceptor())
            .addInterceptor(CommonReqInterceptor())
            //添加缓存拦截器 可传入缓存天数，不传默认7天
            .addInterceptor(CacheInterceptor())
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(30, 5, TimeUnit.MINUTES))
            .retryOnConnectionFailure(true)
//            .addNetworkInterceptor(HttpLogInterceptor())
            // 日志拦截器
            .addInterceptor(LogInterceptor())
//            .addInterceptor(AuthInterceptor())
            .build()
    }
}