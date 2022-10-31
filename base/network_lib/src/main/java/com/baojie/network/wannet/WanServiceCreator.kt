package com.baojie.network.wannet

import com.baojie.baselib.utils.threadutil.ThreadPoolUtils
import com.baojie.network.adapter.WanApiResultCallAdapterFactory
import com.baojie.network.wannet.converter.WanApiResponseConverterFactory
import retrofit2.Retrofit

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2021/11/14 8:36 下午
 */
object WanServiceCreator {


    fun <T> create(serviceClass: Class<T>, baseUrl: String = ""): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(HttpClient.client)
            .addCallAdapterFactory(WanApiResultCallAdapterFactory())
//            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addConverterFactory(WanApiResponseConverterFactory())
            .build()
            .create(serviceClass)
    }

    inline fun <reified T> create(baseUrl: String = ""): T = create(T::class.java, baseUrl)
}