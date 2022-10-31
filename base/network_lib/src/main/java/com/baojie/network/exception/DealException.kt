package com.baojie.network.exception

import android.net.ParseException
import com.baojie.network.exception.ApiException
import com.baojie.network.exception.ApiResultCode
import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.net.UnknownServiceException
import javax.net.ssl.SSLHandshakeException

/**
 * @description: 异常处理
 * @author: wj
 * @date: 2021/1/21 3:30 PM
 */
object DealException {

    fun handlerException(t: Throwable): ApiException {
        val ex: ApiException
        if (t is ApiException) {
            ex = ApiException(t.errorCode, t.errorMsg)
        } else if (t is HttpException) {
            ex = when (t.code()) {
                ApiResultCode.UNAUTHORIZED,
                ApiResultCode.FORBIDDEN,
                    //权限错误，需要实现
                ApiResultCode.NOT_FOUND
                -> ApiException(
                    t.code().toString(),
                    "网络错误"
                )
                ApiResultCode.REQUEST_TIMEOUT,
                ApiResultCode.GATEWAY_TIMEOUT
                -> ApiException(
                    t.code().toString(),
                    "网络连接超时"
                )
                ApiResultCode.INTERNAL_SERVER_ERROR,
                ApiResultCode.BAD_GATEWAY,
                ApiResultCode.SERVICE_UNAVAILABLE
                -> ApiException(
                    t.code().toString(),
                    "服务器错误"
                )
                else -> ApiException(t.code().toString(), "网络错误")
            }
        } else if (t is JsonParseException
            || t is JSONException
            || t is ParseException
        ) {
            ex = ApiException(
                ApiResultCode.PARSE_ERROR,
                "解析错误"
            )
        } else if (t is SocketException) {
            ex = ApiException(
                ApiResultCode.REQUEST_TIMEOUT.toString(),
                "网络连接错误，请重试"
            )
        } else if (t is SocketTimeoutException) {
            ex = ApiException(
                ApiResultCode.REQUEST_TIMEOUT.toString(),
                "网络连接超时"
            )
        } else if (t is SSLHandshakeException) {
            ex = ApiException(
                ApiResultCode.SSL_ERROR,
                "证书验证失败"
            )
            return ex
        } else if (t is UnknownHostException) {
            ex = ApiException(
                ApiResultCode.UNKNOWN_HOST,
                "网络错误，请切换网络重试"
            )
            return ex
        } else if (t is UnknownServiceException) {
            ex = ApiException(
                ApiResultCode.UNKNOWN_HOST,
                "网络错误，请切换网络重试"
            )
        } else if (t is NumberFormatException) {
            ex = ApiException(
                ApiResultCode.UNKNOWN_HOST,
                "数字格式化异常"
            )
        } else {
            ex = ApiException(
                ApiResultCode.UNKNOWN,
                "未知错误"
            )
        }
        return ex
    }
}