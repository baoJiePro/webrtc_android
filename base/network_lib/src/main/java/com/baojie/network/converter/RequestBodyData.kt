package com.baojie.network.converter

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2021/11/13 9:41 上午
 */
data class RequestBodyData<T>(
    val body: BodyData<T>
)

data class BodyData<T>(
    val signature: String,
    val payload: T
)
