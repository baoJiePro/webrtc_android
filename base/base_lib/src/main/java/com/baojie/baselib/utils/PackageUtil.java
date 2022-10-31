package com.baojie.baselib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.List;

public class PackageUtil {

    public static final String PLATFORM = "android";

    public class PackageName {
        public static final String Alipay = "com.eg.android.AlipayGphone";
        public static final String Weixin = "com.tencent.mm";
        public static final String QQ = "com.tencent.mobileqq";
    }

    public static boolean isAppExist(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }

        return false;
    }

//    public static String getVersionName(Context mContext) {
//        if (mContext == null) {
//            return null;
//        }
//        PackageManager packageManager = mContext.getPackageManager();
//        try {
//            /**
//             *  获取应用的版本名的时候，要判断是否是打aar包，如果是，则取配置相应配置里的版本名字
//             *  因为部分接口后台是根据版本名字来判断哪些数据需要返回的，如果不做变动，打aar包获取的版本名字是宿主的
//             */
//            String version;
//            String isPackagingAar = PropertyUtil.getInstance().getValue(PropertyUtil.IS_PACKAGING_AAR);
//            if (PropertyUtil.TRUE.equals(isPackagingAar)) {
//                version = PropertyUtil.getInstance().getValue(PropertyUtil.AAR_VERSION_NAME);
//            } else {
//                PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
//                version = packInfo.versionName;
//            }
//            return version;
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public static int getVersionCode(Context mContext) {
        if (mContext == null) {
            return 7;
        }
        // 获取packagemanager的实例
        PackageManager packageManager = mContext.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            int code = packInfo.versionCode;
            return code;
        } catch (Exception e) {
            return 7;
        }
    }

    public static String getPackageName(Context mContext) {
        String packageName = "";
        PackageManager packageManager = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    mContext.getPackageName(), 0);
            packageName = packageInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageName;
    }

    /**
     * 获取操作系统版本号：如6.0
     *
     * @return
     */
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取设备名字
     *
     * @return
     */
    public static String getDeviceName() {
        return Build.DEVICE;
    }
}
