package com.baojie.baselib.image.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.baojie.baselib.R;
import com.baojie.baselib.image.glide.transformations.CornerGlideTransform;
import com.baojie.baselib.image.glide.transformations.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;



import androidx.annotation.DrawableRes;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2020-01-16 09:37
 * @Version TODO
 */
public class ImageBaseUtil {

    /**
     * 获取默认的加载失败的图片
     * @param context                       上下文
     * @param errorImg                      默认图片
     * @return                              返回drawable资源
     */
    private static Drawable getErrorImage(Context context, int errorImg){
        Drawable drawable;
        if (errorImg != -1){
            drawable = context.getResources().getDrawable(errorImg);
        }else {
            drawable = context.getResources().getDrawable(R.color.qd_base_color_dddddd);
        }
        return drawable;
    }

    /**
     * 获取默认的预加载图片
     * @param context                       上下文
     * @param res                           默认资源
     * @return                              返回drawable资源
     */
    private static Drawable getPlaceholderImage(Context context, int res){
        Drawable drawable;
        if (res != -1){
            drawable = context.getResources().getDrawable(res);
        }else {
            drawable = context.getResources().getDrawable(R.color.qd_base_color_dddddd);
        }
        return drawable;
    }

    /**
     * 加载本地图
     * @param context
     * @param drawableId
     * @param defaultImg
     * @param errorImg
     * @param imageView
     *
     */
    public static void loadImageLocal(Context context ,@DrawableRes int drawableId, int defaultImg, int errorImg,
                                      ImageView imageView){
        if (context!=null && imageView!=null){
            Glide.with(context)
                    .load(drawableId)
                    .placeholder(getPlaceholderImage(context, defaultImg))
                    .error(getErrorImage(context, errorImg))
                    .into(imageView);
        }
    }

    /**
     * 加载网络图片
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     *
     */
    public static void loadImageNet(Context context ,String url, int res, int errorImg,
                                    ImageView imageView){
        if (context!=null && imageView!=null){
            Glide.with(context)
                    .load(url)
                    .placeholder(getPlaceholderImage(context, res))
                    .error(getErrorImage(context, errorImg))
                    .into(imageView);
        }
    }

    /**
     * 加载网络图带淡入效果
     * @param context
     * @param url
     * @param res
     * @param errorImg
     * @param imageView
     */
    public static void loadImageNetWithFade(Context context, String url, int res, int errorImg, ImageView imageView){
        if (context != null && imageView != null){
            DrawableCrossFadeFactory drawableCrossFadeFactory =
                    new DrawableCrossFadeFactory.Builder(300)
                            .setCrossFadeEnabled(true)
                            .build();
            Glide.with(context)
                    .load(url)
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .placeholder(getPlaceholderImage(context, res))
                    .error(getErrorImage(context, errorImg))
                    .into(imageView);
        }
    }

    /**
     * 加载网络图片，并且设置切割圆形，默认没有加载loading
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     */
    public static void loadImageRoundNet(Context context ,String url, ImageView imageView){
        loadImageRoundNet(context, url, -1, -1, imageView);
    }

    public static void loadImageRoundNet(Context context ,String url,int res, int errorImg, ImageView imageView){
        loadImageRound(context, url, res, errorImg, imageView ,false);
    }

    /**
     * 加载本地图片，并且设置切割圆形
     * @param context                       上下文
     * @param res                           图片
     * @param imageView                     控件
     */
    public static void loadImageRoundLocal(Context context ,@DrawableRes int res, ImageView imageView){

        loadImageRoundLocal(context, res, -1, -1, imageView);
    }

    public static void loadImageRoundLocal(Context context ,@DrawableRes int res, int defaultRes, int errorImg, ImageView imageView){

        loadImageRound(context, res, defaultRes, errorImg, imageView , false);
    }

    /**
     * 加载网络图片，并且设置切割圆角，可以设置圆角半径
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     */
    public static void loadImageCorner(Context context ,String url, ImageView imageView , int corner){
        loadImageCorner(context, url, -1, -1, imageView, corner);
    }

    public static void loadImageCorner(Context context ,String url, int res, int errorImg, ImageView imageView, int corner){
        loadImageCorner(context, url, res, errorImg, imageView, false  , corner);
    }

    /**
     * 加载网络图片，并且设置切割圆角
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param isLoad                        是否显示加载动画
     */
    public static void loadImageCorner(Context context ,String url, int res, int errorImg,
                                       ImageView imageView, boolean isLoad , int corner){
        if(context!=null && imageView!=null){
            MultiTransformation<Bitmap> multiTransformation =
                    new MultiTransformation<>(
                            new CenterCrop(),
                            new RoundedCornersTransformation(corner,
                                    0, RoundedCornersTransformation.CornerType.ALL));
            if (isLoad){
                DrawableCrossFadeFactory drawableCrossFadeFactory =
                        new DrawableCrossFadeFactory
                                .Builder(300)
                                .setCrossFadeEnabled(true)
                                .build();
                Glide.with(context)
                        .load(url)
                        .apply(RequestOptions.bitmapTransform(multiTransformation)
                                .placeholder(getPlaceholderImage(context, res))
                                .error(getErrorImage(context, errorImg)))
                        .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(url)
                        .apply(RequestOptions.bitmapTransform(multiTransformation)
                                .placeholder(getPlaceholderImage(context, res))
                                .error(getErrorImage(context, errorImg)))
                        .into(imageView);
            }
        }
    }

    /**
     * 加载本地图片，并且设置切割圆形
     * @param context                       上下文
     * @param img                           图片
     * @param imageView                     控件
     */
    public static void loadImageRound(Context context ,@DrawableRes int img, int res, int errorImg,
                                      ImageView imageView , boolean isLoad){
        if (context!=null && imageView!=null){
            RequestOptions requestOptions = RequestOptions.circleCropTransform();
            if (isLoad){
                DrawableCrossFadeFactory drawableCrossFadeFactory =
                        new DrawableCrossFadeFactory.Builder(300)
                                .setCrossFadeEnabled(true)
                                .build();
                Glide.with(context)
                        .load(img)
                        .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                        .placeholder(getPlaceholderImage(context, res))
                        .error(getErrorImage(context, errorImg))
                        .apply(requestOptions)
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(img)
                        .placeholder(getPlaceholderImage(context, res))
                        .error(getErrorImage(context, errorImg))
                        .apply(requestOptions)
                        .into(imageView);
            }
        }
    }

    /**
     * 加载网络图片，并且设置切割圆形
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param isLoad                        是否显示加载动画
     */
    public static void loadImageRound(Context context ,String url, int res, int errorImg,
                                      ImageView imageView , boolean isLoad){
        if (context!=null && imageView!=null){
            RequestOptions requestOptions = RequestOptions.circleCropTransform();
            if (isLoad){
                DrawableCrossFadeFactory drawableCrossFadeFactory =
                        new DrawableCrossFadeFactory.Builder(300)
                                .setCrossFadeEnabled(true)
                                .build();
                Glide.with(context)
                        .load(url)
                        .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                        .placeholder(getPlaceholderImage(context, res))
                        .error(getErrorImage(context, errorImg))
                        .apply(requestOptions)
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(url)
                        .placeholder(getPlaceholderImage(context, res))
                        .error(getErrorImage(context, errorImg))
                        .apply(requestOptions)
                        .into(imageView);
            }
        }
    }




    /**
     * 加载图片设置圆角
     * @param context
     * @param url
     * @param defaultImg
     * @param iv
     * @param radius
     */
    public static void loadImageWithRound(Context context, String url,
                                          @DrawableRes int defaultImg, ImageView iv, int radius) {
        if (TextUtils.isEmpty(url) || context == null) {
            return;
        }
        Glide.with(context)
                .load(url)
                .placeholder(defaultImg)
                .error(defaultImg)
                .apply(bitmapTransform(new RoundedCornersTransformation(radius, 0,
                        RoundedCornersTransformation.CornerType.ALL)))
                .into(iv);
    }

    /**
     * 加载带有圆角的矩形图片  用glide处理
     *
     * @param path   路径
     * @param round  圆角半径
     * @param errorId  加载失败时的图片
     * @param target 控件
     */
    public static void loadImageWithRound(final Context activity, String path,
                                          final int round, int errorId, final ImageView target) {
        if (path != null && path.length() > 0) {
            Glide.with(activity)
                    .asBitmap()
                    .load(path)
                    .placeholder(errorId)
                    .error(errorId)
                    //设置缓存
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new BitmapImageViewTarget(target) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory
                                    .create(activity.getResources(), resource);
                            //设置绘制位图时要应用的角半径
                            circularBitmapDrawable.setCornerRadius(round);
                            target.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }
    }

    /**
     * 加载网络图片，并且设置切割圆角，切割某个指定的圆角，比如设置图片上面一个圆角，两个圆角，都可以用该方法
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     *
     * @param top                           顶部
     * @param bottom                        底部
     * @param left                          左边
     * @param right                         右边
     */
    public static void loadImageCorner(Context context, String url, int res, int errorImg, ImageView imageView, int corner, boolean top, boolean bottom, boolean left, boolean right){
        loadImageCorner(context, url, res, errorImg, imageView, false, corner, top, bottom, left, right);
    }

    /**
     * 加载网络图片，并且设置切割圆角，切割某个指定的圆角，比如设置图片上面一个圆角，两个圆角，都可以用该方法
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param isLoad                        是否显示加载动画
     * @param top                           顶部
     * @param bottom                        底部
     * @param left                          左边
     * @param right                         右边
     */
    public static void loadImageCorner(Context context , String url, int res, int errorImg,
                                       ImageView imageView , boolean isLoad , int corner ,
                                       boolean top , boolean bottom , boolean left , boolean right){
        if(context!=null && url!=null && imageView!=null){
            CornerGlideTransform multiTransformation = new CornerGlideTransform(context, corner);
            //只是绘制左上角和右上角
            multiTransformation.setExceptCorner(top,bottom, left, right);
            if (isLoad){
                DrawableCrossFadeFactory drawableCrossFadeFactory =
                        new DrawableCrossFadeFactory
                                .Builder(300)
                                .setCrossFadeEnabled(true)
                                .build();
                Glide.with(context)
                        .load(url)
                        .apply(RequestOptions.bitmapTransform(multiTransformation)
                                .placeholder(getPlaceholderImage(context, res))
                                .error(getErrorImage(context, errorImg)))
                        .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(url)
                        .apply(RequestOptions.bitmapTransform(multiTransformation)
                                .placeholder(getPlaceholderImage(context, res))
                                .error(getErrorImage(context, errorImg)))
                        .into(imageView);
            }
        }
    }

    /**
     * 加载图片本地，设置圆角
     * @param context
     * @param url
     * @param res
     * @param errorImg
     * @param imageView
     * @param isLoad
     * @param corner
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    public static void loadImageCorner(Context context , @DrawableRes int url, int res, int errorImg,
                                       ImageView imageView , boolean isLoad , int corner ,
                                       boolean top , boolean bottom , boolean left , boolean right){
        if(context!=null && imageView!=null){
            CornerGlideTransform multiTransformation = new CornerGlideTransform(context, corner);
            //只是绘制左上角和右上角
            multiTransformation.setExceptCorner(top,bottom, left, right);
            if (isLoad){
                DrawableCrossFadeFactory drawableCrossFadeFactory =
                        new DrawableCrossFadeFactory
                                .Builder(300)
                                .setCrossFadeEnabled(true)
                                .build();
                Glide.with(context)
                        .load(url)
                        .apply(RequestOptions.bitmapTransform(multiTransformation)
                                .placeholder(getPlaceholderImage(context, res))
                                .error(getErrorImage(context, errorImg)))
                        .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(url)
                        .apply(RequestOptions.bitmapTransform(multiTransformation)
                                .placeholder(getPlaceholderImage(context, res))
                                .error(getErrorImage(context, errorImg)))
                        .into(imageView);
            }
        }
    }



}
