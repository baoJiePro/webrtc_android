package com.baojie.network.converter

import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Converter
import java.io.OutputStreamWriter
import java.nio.charset.Charset

/**
 *@desc
 *@creator 小灰灰
 *@Time 2019-05-25 - 17:49
 **/
internal class ApiRequestBodyConverter<T>(private val gson: Gson,private val adapter: TypeAdapter<T>):Converter<T, RequestBody> {

    //@Part、@PartMap、@Body 3个注解会调用该转换：用到的是@post + @body一起使用才会封装请求头
    override fun convert(value: T): RequestBody? {
        val buffer = Buffer()
        val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        val jsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.close()

        val signature: String = getSignature(buffer.readByteArray())

        val bodyData = BodyData(signature, value)
        val requestBodyData = RequestBodyData(bodyData)
//        val requestString = gson.toJson(requestBodyData)

        val bufferFinal = Buffer()
        val writerFinal = OutputStreamWriter(bufferFinal.outputStream(), UTF_8)
        val jsonWriterFinal = gson.newJsonWriter(writerFinal)
        val adapterFinal = gson.getAdapter(TypeToken.get(RequestBodyData::class.java))
        adapterFinal.write(jsonWriterFinal, requestBodyData)
        jsonWriterFinal.close()

//        LogUtils.dTag("HttpLog", "修改请求参数：${bufferFinal.readByteString()}")
//        Log.d("HttpLog","修改请求参数：${bufferFinal.readByteString()}")
        return RequestBody.create(MEDIA_TYPE, bufferFinal.readByteString())
    }

    /**
     * 添加签名
     */
    private fun getSignature(source: ByteArray): String {
        return SignatureUtils.encrypt(source, SignatureUtils.SIGNATURE_KEY)
    }

    companion object{
        private val MEDIA_TYPE = MediaType.get("application/json; charset=UTF-8")
        private val UTF_8 = Charset.forName("UTF-8")
    }

}