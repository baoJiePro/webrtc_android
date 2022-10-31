package com.baojie.network.retrofit.intercept;

import com.blankj.utilcode.util.JsonUtils;
import com.blankj.utilcode.util.LogUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2020-04-07 16:56
 * @Version TODO
 */
public class HttpLogInterceptor implements Interceptor {
    public static final String TAG = "HttpLog";

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request original = chain.request();
        String url = original.url().toString();
        String requestString = getRequestInfo(original);
        Response response = chain.proceed(original);
        String responseString = getResponseInfo(response);
        String responseFormat = JsonUtils.formatJson(responseString);
        LogUtils.dTag(TAG, "请求链接：" + url, "请求参数：" + requestString, "请求结果：" + responseFormat);
//        LogUtils.json(TAG, responseString);
        return response;
    }

    private String getResponseInfo(Response response) {
        String str = "";
        if (response == null || !response.isSuccessful()) {
            return str;
        }
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE); // Buffer the entire body.
        } catch (IOException e) {
            e.printStackTrace();
        }
        Buffer buffer = source.buffer();
        Charset charset = StandardCharsets.UTF_8;
        if (contentLength != 0) {
            str = buffer.clone().readString(charset);
        }
        return str;
    }

    private String getRequestInfo(Request request) {
        String str = "";
        if (request == null) {
            return str;
        }
        RequestBody requestBody = request.body();
        if (requestBody == null) {
            return str;
        }
        try {
            Buffer bufferedSink = new Buffer();
            requestBody.writeTo(bufferedSink);
            Charset charset = StandardCharsets.UTF_8;
            str = bufferedSink.readString(charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;

    }
}
