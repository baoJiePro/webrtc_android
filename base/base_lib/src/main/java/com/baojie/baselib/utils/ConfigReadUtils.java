package com.baojie.baselib.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * @Description: 读取相应的配置文件信息
 * @Author baojie@qding.me
 * @Date 2020-02-09 17:04
 * @Version TODO
 */
public class ConfigReadUtils {

    /**
     * 获取manifest <meta-data>的值
     *
     * @param metaKey
     * @return
     */
    public static String getApplicationMetaData(Context context, String metaKey) {
        ApplicationInfo applicationInfo =
                null;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (applicationInfo != null && applicationInfo.metaData != null) {

            String metaStringValue = applicationInfo.metaData.getString(metaKey);
            if (StringUtils.isEmpty(metaStringValue)) {
                return "";
            } else {
                return metaStringValue;
            }
        } else {
            return "";
        }

    }
}
