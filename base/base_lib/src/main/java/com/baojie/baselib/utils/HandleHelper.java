package com.baojie.baselib.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2020-02-21 12:27
 * @Version TODO
 */
public class HandleHelper {
    /**
     * 获取主线程的handler
     */
    public static Handler getHandler(Context context) {
        //获得主线程的looper
        Looper mainLooper = context.getMainLooper();
        //获取主线程的handler
        Handler handler = new Handler(mainLooper);
        return handler;
    }

    /**
     * 延时在主线程执行runnable
     */
    public static boolean postDelayed(Context context, Runnable runnable, long delayMillis) {
        return getHandler(context).postDelayed(runnable, delayMillis);
    }
}
