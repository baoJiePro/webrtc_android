package com.baojie.network.entity


sealed class ApiResult<out T>{
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Failure(val errorCode: String, val errorMsg: String) : ApiResult<Nothing>()
}

inline fun <reified  T> ApiResult<T>.doSuccess(success: (T) -> Unit){
    if (this is ApiResult.Success){
        success(data)
    }
}

inline fun <reified T> ApiResult<T>.doFailure(failure: (ErrorData) -> Unit){
    if (this is ApiResult.Failure){
        val errorData = ErrorData(errorCode, errorMsg)
        failure(errorData)
    }
}

data class ErrorData(
    val errorCode: String,
    val errorMsg: String
)
