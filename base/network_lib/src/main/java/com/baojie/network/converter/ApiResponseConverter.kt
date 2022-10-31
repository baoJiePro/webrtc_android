package com.baojie.network.converter

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.TypeAdapter
import com.baojie.network.exception.ApiException
import com.baojie.network.exception.ApiResultCode
import com.baojie.network.exten.ClassTypeUtils

import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

/**
 * @description: 接口转换
 * @author: wj
 * @date: 2021/1/6 5:06 PM
 */
internal class ApiResponseConverter<T>(private val type: Type, private val gson: Gson, private val adapter: TypeAdapter<T> ) : Converter<ResponseBody, Any?> {
    private val jsonParser: JsonParser by lazy {
        JsonParser()
    }

    override fun convert(value: ResponseBody): Any? {

        val jsonReader = gson.newJsonReader(value.charStream())

        val jsonElement = value.use { _ ->
            jsonParser.parse(jsonReader)
        }
        return if (jsonElement.isJsonObject) {
            with(jsonElement.asJsonObject) {
                var code: String? = null
                get(ApiKeyConstant.KEY_CODE)?.apply {
                    code = this.asString
                }
                if (code == null) {
                    code = "-1"
                }
                var msg: String?
                msg = get(ApiKeyConstant.KEY_MSG)?.asString
                if (msg == null) {
                    msg = ""
                }

                if (code == ApiResultCode.SUCCESS_CODE_200) {
                    val contentJsonElement = get(ApiKeyConstant.KEY_DATA)
                    when {
                        contentJsonElement == null -> {
                            val clazz = ClassTypeUtils.getClass(type, 0)
                            if (clazz == String::class.java) {
                                value.toString()
                            } else {
                                val response = adapter.fromJson(value.toString())
                                response
                            }
                        }
                        contentJsonElement.isJsonNull -> {
                            throw ApiException(ApiResultCode.DATA_EMPTY, msg)
                        }
                        else -> {
                            val clazz = ClassTypeUtils.getClass(type, 0)
                            if (clazz == String::class.java) {
                                contentJsonElement.toString()
                            } else {
                                val response = adapter.fromJson(contentJsonElement.toString())
                                response
                            }
                        }
                    }
                } else {
                    throw ApiException(code!!, msg)
                }
            }
        } else {
            throw ApiException(ApiResultCode.PARSE_ERROR, "response body is not object")
        }
    }
}