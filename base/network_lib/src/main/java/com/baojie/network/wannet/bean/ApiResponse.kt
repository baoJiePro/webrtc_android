package com.baojie.network.wannet.bean

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2021/11/14 9:20 下午
 *  * 如果你的项目中有基类，可以继承BaseResponse，请求时框架可以帮你自动脱壳，自动判断是否请求成功，怎么做：
 * 1.继承 BaseResponse
 * 2.重写isSuccess 方法，编写你的业务需求，根据自己的条件判断数据是否请求成功
 * 3.重写 getResponseCode、getResponseData、getResponseMsg方法，传入你的 code data msg
 */
//data class ApiResponse<T>(val errorCode: Int, val errorMsg: String, val data: T): BaseResponse<T>(){
//
//    override fun isSuccess(): Boolean = errorCode == 0
//
//    override fun getResponseData(): T {
//        return data
//    }
//
//    override fun getCode(): Int {
//        return errorCode
//    }
//
//    override fun getMsg(): String {
//        return errorMsg
//    }
//
//}

sealed class ApiResponse<out T>{
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Failure(val errorCode: String, val errorMsg: String) : ApiResponse<Nothing>()
}

inline fun <reified  T> ApiResponse<T>.doSuccess(success: (T) -> Unit){
    if (this is ApiResponse.Success){
        success(data)
    }
}

inline fun <reified T> ApiResponse<T>.doFailure(failure: (ErrorInfo) -> Unit){
    if (this is ApiResponse.Failure){
        val errorData = ErrorInfo(errorCode, errorMsg)
        failure(errorData)
    }
}

data class ErrorInfo(
    val errorCode: String,
    val errorMsg: String
)
