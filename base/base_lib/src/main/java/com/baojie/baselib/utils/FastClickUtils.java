package com.baojie.baselib.utils;


public class FastClickUtils {

    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 是否是双击事件
     * @return
     */
    public synchronized static boolean isDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            lastClickTime = 0;
            return true;
        }
        lastClickTime = time;
        return false;
    }


}
