package com.baojie.baselib.image;

import android.content.Context;
import android.widget.ImageView;



import androidx.annotation.DrawableRes;

import com.baojie.baselib.R;
import com.baojie.baselib.image.glide.ImageBaseUtil;

/**
 * @Description: 千丁图片展示统一输出类
 * @Author baojie@qding.me
 * @Date 2020-01-19 10:05
 * @Version TODO
 */
public class ImageUtils {

    public static int defaultImg = R.color.qd_base_color_ECEDEF;
    public static int errorImg = R.color.qd_base_color_ECEDEF;
    public static boolean showFade = false;

    /**
     * 设置默认图
     * @param defaultImg
     */
    public static void setDefaultImg(int defaultImg){
        ImageUtils.defaultImg = defaultImg;
    }

    /**
     * 设置加载错误背景
     * @param errorImg
     */
    public static void setErrorImg(int errorImg){
        ImageUtils.errorImg = errorImg;
    }

    /**
     * 设置是否显示加载图片效果
     * @param showFade
     */
    public static void setShowFade(boolean showFade){
        ImageUtils.showFade = showFade;
    }

    /**
     * 加载图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImage(Context context, String url, ImageView imageView){
        ImageBaseUtil.loadImageNet(context, url, defaultImg, errorImg, imageView);
    }

    /**
     * 加载本地图片
     * @param context
     * @param drawableId
     * @param defaultImg
     * @param errorImg
     * @param imageView
     */
    public static void loadImageLocal(Context context, @DrawableRes int drawableId, int defaultImg,
                                      int errorImg, ImageView imageView){
        ImageBaseUtil.loadImageLocal(context, drawableId, defaultImg, errorImg, imageView);
    }

    /**
     * 加载网络图片
     * @param context
     * @param url
     * @param defaultImg
     * @param errorImg
     * @param imageView
     */
    public static void loadImage(Context context, String url, int defaultImg, int errorImg,
                                 ImageView imageView){
        ImageBaseUtil.loadImageNet(context, url, defaultImg, errorImg, imageView);
    }

    /**
     * 淡入淡出效果图片
     * @param context
     * @param url
     * @param defaultImg
     * @param errorImg
     * @param imageView
     */
    public static void loadImageWithFade(Context context, String url, int defaultImg, int errorImg,
                                 ImageView imageView){
        ImageBaseUtil.loadImageNetWithFade(context, url, defaultImg, errorImg, imageView);
    }

    /**
     * 加载网络图片，设置圆形
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImageRoundNet(Context context, String url, ImageView imageView){
        ImageBaseUtil.loadImageRound(context, url, defaultImg, errorImg, imageView, showFade);
    }

    /**
     * 加载本地图片，设置圆形
     * @param context
     * @param img
     * @param imageView
     */
    public static void loadImageRoundLocal(Context context, @DrawableRes int img, ImageView imageView){
        ImageBaseUtil.loadImageRound(context, img, defaultImg, errorImg, imageView, showFade);
    }

    /**
     * 加载网络图，设置圆角
     * @param context
     * @param url
     * @param imageView
     * @param corner
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    public static void loadImageCornerNet(Context context , String url, ImageView imageView, int corner,
                                          boolean top , boolean bottom , boolean left , boolean right){
        ImageBaseUtil.loadImageCorner(context, url, defaultImg, errorImg, imageView, showFade, corner,
                top, bottom, left, right);
    }

    /**
     * 加载本地图片，设置圆角
     * @param context
     * @param url
     * @param imageView
     * @param corner
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    public static void loadImageCornerLocal(Context context , @DrawableRes int url, ImageView imageView, int corner,
                                          boolean top , boolean bottom , boolean left , boolean right){
        ImageBaseUtil.loadImageCorner(context, url, defaultImg, errorImg, imageView, showFade, corner,
                top, bottom, left, right);
    }


}
