package com.baojie.baselib.xlog

import com.baojie.baselib.BuildConfig
import com.blankj.utilcode.util.Utils

/**
 * @Description:
 * @Author baoJie
 * @Date 2022/9/30 13:30
 */
object XLogUtils {

    private const val DEFAULT_TAG = "anJin"

    fun loadLibrary() {
        System.loadLibrary("c++_shared")
        System.loadLibrary("marsxlog")
    }

    fun init(isDebug: Boolean = false) {
        val logPath = "${Utils.getApp().filesDir}/xlog/log"
        val cachePath = "${Utils.getApp().filesDir}/xlog/temp"
        val xlog = Xlog()
        Log.setLogImp(xlog)
        if (isDebug) {
            Log.setConsoleLogOpen(true)
            Log.appenderOpen(Xlog.LEVEL_DEBUG, Xlog.AppednerModeAsync, cachePath, logPath, "qd", 0)
        } else {
            Log.setConsoleLogOpen(false)
            Log.appenderOpen(Xlog.LEVEL_INFO, Xlog.AppednerModeAsync, cachePath, logPath, "qd", 0)
        }
    }

    fun v(msg: String) {
        v(DEFAULT_TAG, msg)
    }

    fun v(format: String, vararg obj: Any?) {
        Log.v(DEFAULT_TAG, format, *obj)
    }

    fun f(msg: String) {
        f(DEFAULT_TAG, msg)
    }

    fun f(format: String, vararg obj: Any?) {
        Log.f(DEFAULT_TAG, format, *obj)
    }

    fun e(msg: String) {
        e(DEFAULT_TAG, msg)
    }

    fun e(format: String, vararg obj: Any?) {
        Log.e(DEFAULT_TAG, format, *obj)
    }

    fun w(msg: String) {
        w(DEFAULT_TAG, msg)
    }

    fun w(format: String, vararg obj: Any?) {
        Log.w(DEFAULT_TAG, format, *obj)
    }

    fun d(msg: String) {
        d(DEFAULT_TAG, msg)
    }

    fun d(format: String, vararg obj: Any?) {
        Log.d(DEFAULT_TAG, format, *obj)
    }






    fun logClose(){
        Log.appenderClose()
    }

    //当日志写入模式为异步时，调用该接口会把内存中的日志写入到文件。
    fun flush() {
        Log.appenderFlush()
    }
}