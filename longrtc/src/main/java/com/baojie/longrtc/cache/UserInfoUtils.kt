package com.baojie.longrtc.cache

import com.baojie.baselib.utils.MKUtil

/**
 * @Description:
 * @Author baoJie
 * @Date 2022/10/31 16:00
 */
object UserInfoUtils {

    private const val USER_INFO = "user_info"

    private const val KEY_NAME = "name"

    //获取用户名
    fun getUserName(): String {
        return MKUtil.getValue(USER_INFO, KEY_NAME)
    }

    //设置用户名
    fun setUserName(name: String){
        MKUtil.setValue(USER_INFO, KEY_NAME, name)
    }
}