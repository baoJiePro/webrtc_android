package com.baojie.network.retrofit.intercept;

/**
 * Created by kuangbiao on 2018/9/21.
 */

import android.os.SystemClock;
import android.util.Log;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * 重试拦截器
 */
public class RetryIntercepter implements Interceptor {

    private static final String TAG = "RetryIntercepter";
    public int maxRetry;//最大重试次数
    private int retryNum = 0;//假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

    public RetryIntercepter(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Log.d(TAG, request.url() + " intercept retryNum = " + retryNum + " thread " + Thread.currentThread().getName());
        Response response = chain.proceed(request);
        while (!response.isSuccessful() && retryNum < maxRetry) {
            SystemClock.sleep(2 * 1000);
            retryNum++;
            response = chain.proceed(request);
        }
        return response;
    }
}
