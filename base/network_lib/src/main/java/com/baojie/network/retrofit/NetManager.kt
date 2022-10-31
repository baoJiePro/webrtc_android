package com.baojie.network.retrofit

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2021/6/10 3:02 下午
 */
object NetManager {

    /**
     * 初始化
     */
    fun init(netConfig: NetConfig){
        RtServiceCreator.configNet(netConfig)
    }
}