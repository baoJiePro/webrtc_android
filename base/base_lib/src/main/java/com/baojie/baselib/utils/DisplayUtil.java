package com.baojie.baselib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * @ProjectName: BizOwnerAndroid
 * @Package: com.qding.common.library.framework.utils
 * @ClassName: DisplayUtil
 * @Description: View相关Util
 * @Author: lestin.yin
 * @CreateDate: 2019-12-17 09:46
 * @Version: 1.0
 */
public class DisplayUtil {
    /**
     * 获取屏幕的宽带
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        if(disp != null) {
            disp.getMetrics(dm);
        }
        return dm.widthPixels;
    }
    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将dp转换为px
     * @param dp
     * @return
     */
    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    /**
     * 将px转换为dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale - 0.5F);
    }
}
