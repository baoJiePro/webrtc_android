package com.qding.baselib.image

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.baojie.baselib.R

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2021/1/19 7:13 PM
 */
object GlideUtils {
    var placeHolderImageView = R.color.qd_base_color_ECEDEF
    var errorImage = R.color.qd_base_color_ECEDEF

    /**
     * 设置默认图
     * @param defaultImg
     */
    fun setDefaultImg(defaultImg: Int) {
        placeHolderImageView = defaultImg
    }

    /**
     * 设置加载错误背景
     * @param errorImg
     */
    fun setErrorImg(errorImg: Int) {
        errorImage = errorImg
    }


    /**
     * 加载本地图片
     */
    fun ImageView.loadGlideImage(context: Context, @RawRes @DrawableRes drawableId: Int){

    }


}