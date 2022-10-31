package com.baojie.network.exception

import java.io.IOException

class ApiException(val errorCode:String,val errorMsg:String): IOException()
