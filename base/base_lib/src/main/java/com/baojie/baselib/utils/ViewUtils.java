package com.baojie.baselib.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;

/**
 * @ProjectName: android
 * @Package: com.qianding.plugin.common.library.utils
 * @ClassName: ViewUtils
 * @Description: View 工具类
 * @Author: lestin.yin
 * @CreateDate: 2019-12-10 09:52
 * @Version: 1.0
 */
public class ViewUtils {
    /**
     * px to dp
     */
    public static int  px2dp(float px ) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int)(px / scale + 0.5f);
    }
    /**
     * dp to px
     */
    public static int dp2px(int dp){
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    /**
     * 根据view生成bitmap
     * @param view
     * @return
     */
    public static Bitmap getBitmapFromView4(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    public static Bitmap getBitmapFromView8(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * 创建带颜色背景的drawable
     * @param color
     * @param radius
     * @return
     */
    public static Drawable createDrawable(String color, int radius){
        int fillColor;
        fillColor = Color.parseColor(color);//内部填充颜色
        GradientDrawable drawable = new GradientDrawable();//创建drawable
        drawable.setColor(fillColor);
        drawable.setCornerRadius(radius);
        return drawable;
    }

}
