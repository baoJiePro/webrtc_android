package com.baojie.network.retrofit

import com.blankj.utilcode.util.ToastUtils

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2021/6/10 10:14 上午
 */
object RtServiceCreator {

    var netConfigData: NetConfig = NetConfig.Builder().build()

    /**
     * 配置netConfig，如果不配置使用默认的
     */
    fun configNet(netConfig: NetConfig) {
        netConfigData = netConfig
    }


    fun <T> create(serviceClass: Class<T>, baseUrl: String = ""): T {
        return if (baseUrl.isEmpty()) {
            require(
                netConfigData.getGlobalUrl().isNotEmpty()
            ) { ToastUtils.showShort("请设置globalUrl") }
            netConfigData.getRetrofitMap()[netConfigData.getGlobalUrl()]!!.create(serviceClass)
        } else {
            if (netConfigData.getRetrofitMap()[baseUrl] != null) {
                netConfigData.getRetrofitMap()[baseUrl]!!.create(serviceClass)
            } else {
                val retrofit = netConfigData.getRetrofit(baseUrl)
                netConfigData.getRetrofitMap()[baseUrl] = retrofit
                retrofit.create(serviceClass)
            }
        }
    }

    inline fun <reified T> create(baseUrl: String = ""): T = create(T::class.java, baseUrl)
}