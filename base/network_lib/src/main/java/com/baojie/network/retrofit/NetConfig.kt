package com.baojie.network.retrofit

import com.baojie.network.retrofit.intercept.HttpLogInterceptor
import com.baojie.network.retrofit.intercept.TimeoutInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.HashMap
import java.util.concurrent.TimeUnit

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2021/6/10 10:22 上午
 */
class NetConfig {

    private lateinit var retrofitMap: HashMap<String, Retrofit>
    private lateinit var okClient: OkHttpClient
    private lateinit var globalBaseUrl: String
    private var callAdapterFactory: Array<out CallAdapter.Factory>? = null
    private var converterFactory: Array<out Converter.Factory>? = null

    constructor(
        globalBaseUrl: String,
        okClient: OkHttpClient,
        converterFactory: Array<out Converter.Factory>?,
        callAdapterFactory: Array<out CallAdapter.Factory>?,
        retrofitMap: HashMap<String, Retrofit>
    ){
        this.globalBaseUrl = globalBaseUrl
        this.okClient = okClient
        this.converterFactory = converterFactory
        this.callAdapterFactory = callAdapterFactory
        this.retrofitMap = retrofitMap

        initGlobalRetrofit()
    }

    private fun initGlobalRetrofit() {
        if (globalBaseUrl.isNotEmpty()){
            val retrofit = getRetrofit(globalBaseUrl)
            retrofitMap[globalBaseUrl] = retrofit
        }
    }

    internal fun getRetrofit(baseUrl: String): Retrofit{
        return Retrofit.Builder().apply {
            baseUrl(baseUrl)
            converterFactory?.forEach {
                addConverterFactory(it)
            }
            callAdapterFactory?.forEach {
                addCallAdapterFactory(it)
            }
            client(okClient)
        }.build()
    }

    fun getRetrofitMap(): HashMap<String, Retrofit>{
        return retrofitMap
    }

    fun getOkClient(): OkHttpClient{
        return okClient
    }

    fun getGlobalUrl(): String{
        return globalBaseUrl
    }


    class Builder {

        private val retrofitMap = hashMapOf<String, Retrofit>()

        private var globalBaseUrl: String = ""

        private var converterFactory: Array<out Converter.Factory>? = null
        private var callAdapterFactory: Array<out CallAdapter.Factory>? = null

        private var okClient: OkHttpClient = OkHttpClient.Builder()
            .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(Constants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addNetworkInterceptor(HttpLogInterceptor())
            .addInterceptor(TimeoutInterceptor())
            .build()

        /**
         * 外部可直接传入OkHttpClient，若不传使用默认
         */
        fun netClient(okHttpClient: OkHttpClient): Builder {
            okClient = okHttpClient
            return this
        }

        /**
         * 外部可直接传入retrofit
         */
        fun netRetrofit(retrofit: Retrofit): Builder {
            val url = retrofit.baseUrl().toString()
            retrofitMap[url] = retrofit
            return this
        }

        /**
         * 设置超时时间，默认15s
         */
        fun withTimeOut(write: Long, read: Long, connect: Long): Builder {
            okClient = okClient.newBuilder()
                .writeTimeout(write, TimeUnit.SECONDS)
                .readTimeout(read, TimeUnit.SECONDS)
                .connectTimeout(connect, TimeUnit.SECONDS)
                .build()
            return this
        }

        /**
         * 添加拦截器
         */
        fun withAddInterceptor(vararg interceptors: Interceptor): Builder {
            val newBuilder = okClient.newBuilder()
            interceptors.forEach {
                newBuilder.addInterceptor(it)
            }
            okClient = newBuilder.build()
            return this
        }

        /**
         * 设置全局baseUrl
         */
        fun withGlobalBaseUrl(baseUrl: String): Builder {
            globalBaseUrl = baseUrl
            return this
        }

        /**
         * 添加Converter.Factory
         */
        fun withAddConverterFactory(vararg factory: Converter.Factory): Builder {
            converterFactory = factory
            return this
        }

        /**
         * 添加CallAdapter.Factory
         */
        fun withAddCallAdapterFactory(vararg factory: CallAdapter.Factory): Builder {
            callAdapterFactory = factory
            return this
        }

        fun build(): NetConfig {
            return NetConfig(
                globalBaseUrl,
                okClient,
                converterFactory,
                callAdapterFactory,
                retrofitMap
            )
        }
    }
}