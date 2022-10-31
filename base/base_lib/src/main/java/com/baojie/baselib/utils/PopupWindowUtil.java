package com.baojie.baselib.utils;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.PopupWindow;

/**
 * PopupWindow弹窗
 *
 * @author wj
 * @date 2018/7/19
 */
public class PopupWindowUtil {
    /**
     * 显示视图中间操作的对话框
     *
     * @param activity
     * @param resource 布局文件资源ID
     * @return
     */
    public static PopupWindow showCenterDialog(final Activity activity, int resource) {
        View contentView = View.inflate(activity, resource, null);
        PopupWindow popupWindow = new PopupWindow(contentView,
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setBackgroundAlpha(activity,0.5f);
        popupWindow.setContentView(contentView);
        popupWindow.showAtLocation(activity.getWindow().getDecorView(),
                Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(activity,1.0f);
            }
        });
        return popupWindow;
    }

    /**
     * 显示底部操作的对话框，带有缩放动画
     *
     * @param activity
     * @param resource 布局文件资源ID
     * @return
     */
    public static PopupWindow showDialog(Activity activity, int resource) {
        View contentView = View.inflate(activity, resource, null);
        PopupWindow popupWindow = new PopupWindow(contentView,
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setContentView(contentView);
        ScaleAnimation sa = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(300);
        contentView.startAnimation(sa);
        popupWindow.showAtLocation(activity.getWindow().getDecorView(),
                Gravity.BOTTOM, 0, 0);
        return popupWindow;
    }

    /**
     * 显示底部操作的对话框
     *
     * @param activity
     * @param resource 布局文件资源ID
     * @return
     */
    public static PopupWindow showBottomDialog(Activity activity, int resource) {
        View contentView = View.inflate(activity, resource, null);
        PopupWindow popupWindow = new PopupWindow(contentView,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setBackgroundAlpha(activity,0.5f);
        popupWindow.setOutsideTouchable(false);

        popupWindow.showAtLocation(activity.getWindow().getDecorView(),
                Gravity.BOTTOM, 0, 0);

        return popupWindow;
    }


    /**
     * 创建对话框，带有缩放动画
     *
     * @param context
     * @param resource 布局文件资源ID
     * @return
     */
    public static PopupWindow createDialog(Context context, int resource) {
        View contentView = View.inflate(context, resource, null);
        PopupWindow popupWindow = new PopupWindow(contentView,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(contentView);
        ScaleAnimation sa = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(300);
        contentView.startAnimation(sa);
        return popupWindow;
    }

    /**
     * 设置背景颜色
     *
     * @param bgAlpha
     */
    public static void setBackgroundAlpha(Context mContext, float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity) mContext).getWindow().setAttributes(lp);
    }
}
