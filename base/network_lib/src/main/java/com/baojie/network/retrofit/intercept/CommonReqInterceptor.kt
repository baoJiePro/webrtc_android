package com.baojie.network.retrofit.intercept

import com.alibaba.fastjson.JSON
import com.baojie.network.converter.SignatureUtils

import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import java.util.*

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2021/11/12 7:30 下午
 */
class CommonReqInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (request.body() is FormBody){

            val builder = FormBody.Builder()
            val body = request.body() as FormBody
            val jsonParamObj = JSONObject()
            val treeMap = TreeMap<String, String>()
            for (i in 0 until body.size()){
                jsonParamObj.put(body.name(i), body.value(i))
                treeMap[body.name(i)] = body.value(i)
            }

//            val sourceParam = gson.toJson(treeMap)
            val sourceParam = JSON.toJSONString(treeMap)
            val signature = getSignature(sourceParam.toByteArray())

            val bodyParamObj = JSONObject()
            bodyParamObj.put("payload", jsonParamObj)
            bodyParamObj.put("signature", signature)
            builder.add("body", bodyParamObj.toString())
            request = request.newBuilder().post(builder.build()).build()

        }
        return chain.proceed(request)
    }

    /**
     * 添加签名
     */
    private fun getSignature(source: ByteArray): String {
        return SignatureUtils.encrypt(source, SignatureUtils.SIGNATURE_KEY)
    }
}