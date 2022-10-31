package com.baojie.network.exception

object ApiResultCode {

    //对应HTTP的状态码
    const val UNAUTHORIZED = 401
    const val FORBIDDEN = 403
    const val NOT_FOUND = 404
    const val REQUEST_TIMEOUT = 408
    const val INTERNAL_SERVER_ERROR = 500
    const val BAD_GATEWAY = 502
    const val SERVICE_UNAVAILABLE = 503
    const val GATEWAY_TIMEOUT = 504

    const val UNKNOWN = "2000"
    const val PARSE_ERROR = "2001"
    const val UNKNOWN_HOST = "2002"
    const val SSL_ERROR = "2003"
    const val DATA_EMPTY = "2004"

    const val SUCCESS_CODE_200 = "200"
    const val SUCCESS_CODE_0 = "0"

}
