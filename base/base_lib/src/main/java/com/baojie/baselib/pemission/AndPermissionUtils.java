package com.baojie.baselib.pemission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.baojie.baselib.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2020-02-08 17:34
 * @Version TODO
 */
public class AndPermissionUtils {
    public static final String TAG="AndPermissionUtils";

    /**
     * 申请非必须权限
     * @param context
     * @param listener
     * @param permissions
     */
    @SuppressLint("WrongConstant")
    public static void requestPermission(Context context, OnRequestPermissionListener listener, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (AndPermission.hasPermissions(context, permissions)) {
                if (listener != null)
                    listener.onGranted();
                return;
            }
            AndPermission.with(context)
                    .runtime()
                    .permission(permissions)
                    .onGranted(data -> {
                        listener.onGranted();
                    })
                    .onDenied(data -> {
                        listener.onDenied(false);

                    })
                    .start();
        } else {
            listener.onGranted();
        }
    }

    /**
     * 申请非必须权限
     * @param fragment
     * @param listener
     * @param permissions
     */
    @SuppressLint("WrongConstant")
    public static void requestPermission(Fragment fragment, OnRequestPermissionListener listener, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (AndPermission.hasPermissions(fragment, permissions)) {
                if (listener != null)
                    listener.onGranted();
                return;
            }
            AndPermission.with(fragment)
                    .runtime()
                    .permission(permissions)
                    .onGranted(data -> {
                        listener.onGranted();
                    })
                    .onDenied(data -> {
                        listener.onDenied(false);

                    })
                    .start();
        } else {
            listener.onGranted();
        }
    }

    /**
     * 请求必须的权限
     * @param context
     * @param listener
     * @param reqCode  设置界面回来的码，在onActivityResult中reqCode码进行处理
     * @param permissions
     */
    @SuppressLint("WrongConstant")
    public static void requestMustPermission(Activity context, OnRequestPermissionListener listener, int reqCode, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (AndPermission.hasPermissions(context, permissions)) {
                if (listener != null)
                    listener.onGranted();
                return;
            }
            AndPermission.with(context)
                    .runtime()
                    .permission(permissions)
                    .onGranted(data -> {
                        listener.onGranted();
                    })
                    .onDenied(data -> {
                        listener.onDenied(false);
                        //申请失败
                        if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {

                            List<String> strings = Permission.transformText(context, permissions);

                            new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert)
                                    .setTitle("申请权限")
                                    .setMessage("请去设置界面设置权限" + TextUtils.join("，\n", strings))
                                    .setPositiveButton("确定",(dialog,witch)->{
                                        requestAgain(context,listener, reqCode, permissions);

                                    })
                                    .setNegativeButton("取消",(dialog,witch)->{
                                        listener.onDenied(true);

                                    })
                                    .setCancelable(false)
                                    .create()
                                    .show();


                        }

                    })
                    .start();
        } else {
            listener.onGranted();
        }
    }


    /**
     * 判断是否有该权限
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermissions(Context context, String... permissions){
        return AndPermission.hasPermissions(context, permissions);
    }





    public interface OnRequestPermissionListener {
        void onGranted();

        void onDenied(boolean isAlways);

    }



    public static void requestAgain(Activity activity, OnRequestPermissionListener listener, int reqCode, String... permissions) {
        // 这些权限被用户总是拒绝。
        AndPermission.with(activity)
                .runtime()
                .setting()
                .start(reqCode);
    }
}
