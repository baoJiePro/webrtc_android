package com.baojie.network.adapter


import com.baojie.network.entity.ApiResult
import com.baojie.network.exception.DealException
import com.baojie.network.wannet.bean.ApiResponse
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class WanApiResultCallAdapterFactory : CallAdapter.Factory(){
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *> {
        /*凡是检测不通过的，直接抛异常，提示使用者返回值类型格式不对
        因为ApiResultCallAdapterFactory是使用者显式设置使用的*/


        //以下是检查是否是 Call<ApiResult<T>> 类型的returnType

        //检查returnType是否是Call<T>类型的
        check(getRawType(returnType) == Call::class.java) { "$returnType must be retrofit2.Call." }
        check(returnType is ParameterizedType) { "$returnType must be parameterized. Raw types are not supported" }

        //取出Call<T> 里的T，检查是否是ApiResult<T>
        val apiResultType = getParameterUpperBound(0, returnType)
        check(getRawType(apiResultType) == ApiResponse::class.java) { "$apiResultType must be ApiResult." }
        check(apiResultType is ParameterizedType) { "$apiResultType must be parameterized. Raw types are not supported" }

        //取出ApiResult<T>中的T 也就是API返回数据对应的数据类型
        val dataType = getParameterUpperBound(0, apiResultType)

        return WanApiResultCallAdapter<Any>(dataType)


    }

}

class WanApiResultCallAdapter<T>(private val type: Type) : CallAdapter<T, Call<ApiResponse<T>>> {
    override fun responseType(): Type = type

    override fun adapt(call: Call<T>): Call<ApiResponse<T>> {
        return WanApiResultCall(call)
    }
}

class WanApiResultCall<T>(private val delegate: Call<T>) : Call<ApiResponse<T>> {
    /**
     * 该方法会被Retrofit处理suspend方法的代码调用，并传进来一个callback,如果你回调了callback.onResponse，那么suspend方法就会成功返回
     * 如果你回调了callback.onFailure那么suspend方法就会抛异常
     *
     * 所以我们这里的实现是永远回调callback.onResponse,只不过在请求成功的时候返回的是ApiResult.Success对象，
     * 在失败的时候返回的是ApiResult.Failure对象，这样外面在调用suspend方法的时候就不会抛异常，一定会返回ApiResult.Success 或 ApiResult.Failure
     */
    override fun enqueue(callback: Callback<ApiResponse<T>>) {
        //delegate 是用来做实际的网络请求的Call<T>对象，网络请求的成功失败会回调不同的方法
        delegate.enqueue(object : Callback<T> {

            /**
             * 网络请求成功返回，会回调该方法（无论status code是不是200）
             */
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {//http status 是200+
                    //这里担心response.body()可能会为null(还没有测到过这种情况)，所以做了一下这种情况的处理，
                    // 处理了这种情况后还有一个好处是我们就能保证我们传给ApiResult.Success的对象就不是null，这样外面用的时候就不用判空了
                    if (response.body() != null){
                        val apiResult = ApiResponse.Success(response.body()!!)
                        callback.onResponse(this@WanApiResultCall, Response.success(apiResult))
                    }else{
                        val failureApiResult = ApiResponse.Failure(response.code().toString(),
                            response.message())
                        callback.onResponse(this@WanApiResultCall, Response.success(failureApiResult))
                    }
                } else {//http status错误
                    val failureApiResult = ApiResponse.Failure(response.code().toString(),
                        response.message())
                    callback.onResponse(this@WanApiResultCall, Response.success(failureApiResult))
                }

            }

            /**
             * 在网络请求中发生了异常，会回调该方法
             *
             * 对于网络请求成功，但是业务失败的情况，我们也会在对应的Interceptor中抛出异常，这种情况也会回调该方法
             */
            override fun onFailure(call: Call<T>, t: Throwable) {
                val failureResult = ApiResponse.Failure(
                    DealException.handlerException(t).errorCode,
                    DealException.handlerException(t).errorMsg)
                callback.onResponse(this@WanApiResultCall, Response.success(failureResult))
            }
        })
    }

    override fun clone(): Call<ApiResponse<T>> = WanApiResultCall(delegate.clone())

    override fun execute(): Response<ApiResponse<T>> {
        throw UnsupportedOperationException("ApiResultCall does not support synchronous execution")
    }


    override fun isExecuted(): Boolean {
        return delegate.isExecuted
    }

    override fun cancel() {
        delegate.cancel()
    }

    override fun isCanceled(): Boolean {
        return delegate.isCanceled
    }

    override fun request(): Request {
        return delegate.request()
    }

    override fun timeout(): Timeout {
        return delegate.timeout()
    }
}