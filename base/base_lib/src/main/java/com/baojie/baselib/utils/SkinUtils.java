package com.baojie.baselib.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;

/**
 * The type Skin utils.
 *
 * @Description: 换肤工具类
 * @Author baojie @qding.me
 * @Date 2020 -04-13 14:48
 * @Version
 */
public class SkinUtils {

    /**
     * 给imgview 上色，前提是采用src背景图标
     *
     * @param view    ImageView
     * @param colorId color资源
     */
    public static void setImageViewColorWithSrc(ImageView view, int colorId) {
        //mutate()
        Drawable modeDrawable = view.getDrawable().mutate();
//        Drawable modeDrawable = view.getDrawable();
        Drawable temp = DrawableCompat.wrap(modeDrawable);
        ColorStateList colorStateList = ColorStateList.valueOf(view.getResources().getColor(colorId));
        DrawableCompat.setTintList(temp, colorStateList);
        view.setImageDrawable(temp);
    }

    /**
     * 给iview上色，前提是采用background背景图标
     *
     * @param view    the view
     * @param colorId the color id
     */
    public static void setImageColorWithBackground(View view, int colorId) {
        Drawable mutate = view.getBackground().mutate();
        Drawable temp = DrawableCompat.wrap(mutate);
        ColorStateList colorStateList = ColorStateList.valueOf(view.getResources().getColor(colorId));
        DrawableCompat.setTintList(temp, colorStateList);
        view.setBackground(temp);
    }

    /**
     * 给view上背景色
     *
     * @param view  the view
     * @param color 颜色值
     */
    public static void setImageColorWithBackground(ImageView view, String color) {
        if (!StringUtils.isEmpty(color)) {
            Drawable mutate = view.getBackground().mutate();
            Drawable temp = DrawableCompat.wrap(mutate);
            DrawableCompat.setTint(temp, Color.parseColor(color));
            view.setBackground(temp);
        }
    }

    /**
     * 给view上色
     *
     * @param view       the view
     * @param drawableId the drawable id
     * @param colorId    the color id
     */
    public static void setImageColorWithBackground(ImageView view, int drawableId, int colorId) {
        Drawable drawable = view.getContext().getResources().getDrawable(drawableId);
        Drawable temp = DrawableCompat.wrap(drawable);
        ColorStateList colorStateList = ColorStateList.valueOf(view.getResources().getColor(colorId));
        DrawableCompat.setTintList(temp, colorStateList);
        view.setBackground(temp);
    }

    /**
     * 设置checkbox
     *
     * @param context    the context
     * @param view       the view
     * @param drawableId the drawable id
     * @param colorId    the color id
     */
    public static void setCheckBoxData(Context context, TextView view, int drawableId, int colorId) {
        Drawable drawable = context.getResources().getDrawable(drawableId).mutate();
        Drawable temp = DrawableCompat.wrap(drawable);
        ColorStateList colorStateList = ColorStateList.valueOf(view.getResources().getColor(colorId));
        DrawableCompat.setTintList(temp, colorStateList);
        view.setCompoundDrawablesWithIntrinsicBounds(null, null, temp, null);
    }

    /**
     * 设置checkbox
     *
     * @param context    the context
     * @param imageView  the image view
     * @param drawableId the drawable id
     * @param colorId    the color id
     */
    public static void setCheckBoxData(Context context, ImageView imageView, int drawableId, int colorId) {
        Drawable drawable = context.getResources().getDrawable(drawableId).mutate();
        Drawable temp = DrawableCompat.wrap(drawable);
        ColorStateList colorStateList = ColorStateList.valueOf(context.getResources().getColor(colorId));
        DrawableCompat.setTintList(temp, colorStateList);
        imageView.setImageDrawable(temp);
    }


    /**
     * 设置RadioButton的icon，带drawablepadding
     *
     * @param context             the context
     * @param radioButton         the radio button
     * @param isChecked           the is checked
     * @param checkedDrawableId   the checked drawable id
     * @param uncheckedDrawableId the unchecked drawable id
     * @param colorId             the color id
     * @param drawablePadding     the drawable padding
     */
    public static void setRbCheckBoxImage(Context context, RadioButton radioButton, boolean isChecked,
                                          int checkedDrawableId, int uncheckedDrawableId, int colorId,
                                          int drawablePadding) {
        Drawable drawable = context.getResources().getDrawable(checkedDrawableId).mutate();
        Drawable unCheckDrawable = context.getResources().getDrawable(uncheckedDrawableId).mutate();
        Drawable temp = DrawableCompat.wrap(drawable);
        ColorStateList colorStateList = ColorStateList.valueOf(context.getResources().getColor(colorId));
        DrawableCompat.setTintList(temp, colorStateList);
        temp.setBounds(0, 0, 48, 48);
        unCheckDrawable.setBounds(0, 0, 48, 48);
        if (isChecked) {
            radioButton.setCompoundDrawablePadding((int) drawablePadding);
            radioButton.setCompoundDrawables(temp, null, null, null);
        } else {
            radioButton.setCompoundDrawablePadding(drawablePadding);
            radioButton.setCompoundDrawables(unCheckDrawable, null, null, null);
        }
    }

    /**
     * 设置RadioButton的icon
     *
     * @param context             the context
     * @param checkBox            the check box
     * @param isChecked           the is checked
     * @param checkedDrawableId   the checked drawable id
     * @param uncheckedDrawableId the unchecked drawable id
     * @param colorId             the color id
     */
    public static void setRbCheckBoxImage(Context context, CheckBox checkBox, boolean isChecked,
                                          int checkedDrawableId, int uncheckedDrawableId, int colorId) {
        Drawable drawable = context.getResources().getDrawable(checkedDrawableId).mutate();
        Drawable unCheckDrawable = context.getResources().getDrawable(uncheckedDrawableId).mutate();
        Drawable temp = DrawableCompat.wrap(drawable);
        ColorStateList colorStateList = ColorStateList.valueOf(context.getResources().getColor(colorId));
        DrawableCompat.setTintList(temp, colorStateList);
        temp.setBounds(0, 0, 48, 48);
        unCheckDrawable.setBounds(0, 0, 48, 48);
        if (isChecked) {
            checkBox.setCompoundDrawables(temp, null, null, null);
        } else {
            checkBox.setCompoundDrawables(unCheckDrawable, null, null, null);
        }
    }
}
