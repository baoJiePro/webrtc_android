package com.baojie.baselib.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * description:
 *
 * @author zhuzhaoyang
 * @date 2019/3/4
 */
public class PropertyUtil {
    public static final String IS_PACKAGING_AAR = "IS_PACKAGING_AAR";
    public static final String AAR_VERSION_NAME = "AAR_VERSION_NAME";
    public static final String TRUE = "true";

    private static PropertyUtil instance;
    private Properties properties;

    private PropertyUtil(Context context) {
        properties = new Properties();
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open("commonConfig.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PropertyUtil getInstance(Context context) {
        if (instance == null) {
            instance = new PropertyUtil(context);
        }
        return instance;
    }

    public String getValue(String key) {
        return properties.getProperty(key);
    }

}
