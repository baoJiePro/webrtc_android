package com.baojie.network.wannet.bean

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2021/11/14 7:58 下午
 */
abstract class BaseResponse<T>{
    //抽象方法，用户的基类继承该类时，需要重写该方法
    abstract fun isSuccess(): Boolean

    abstract fun getResponseData(): T

    abstract fun getCode(): Int

    abstract fun getMsg(): String
}