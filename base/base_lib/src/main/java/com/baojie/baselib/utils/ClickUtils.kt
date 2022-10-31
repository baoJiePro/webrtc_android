package com.baojie.baselib.utils

import android.os.SystemClock
import android.util.Log
import android.view.View
import com.blankj.utilcode.util.LogUtils

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2021/5/30 12:05 下午
 */
object ClickUtils {

    /**
     * 在一定时间内快速点击响应事件，例如在2s内点击5次才调用callBack方法
     * @param view 快速点击的view
     * @param count 点击次数 5
     * @param duration 时间间隔 毫秒 2000
     * @param callBack 响应处理
     */
    fun fastClickInTime(view: View, count: Int, duration: Long, callBack:() -> Unit){
        var hits = LongArray(count)
        view.setOnClickListener {
            System.arraycopy(hits, 1, hits, 0, hits.size - 1)
            hits[hits.size - 1] = System.currentTimeMillis()
            LogUtils.d(hits)
            if (hits[0] >= (System.currentTimeMillis() - duration)){
                hits = LongArray(count)
                callBack()
            }
        }

    }

}