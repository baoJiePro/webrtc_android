package com.baojie.baselib.utils;

import android.content.Context;
import android.os.Parcelable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.tencent.mmkv.MMKV;

import java.lang.reflect.Type;

import androidx.annotation.Nullable;

/**
 * @Description :
 * TODO 基于'mmaping'原理高性能键值对储存库
 * TODO 默认实习线程和进程数据同步
 * TODO 腾讯开源库地址"https://github.com/Tencent/MMKV"
 * @Author :YangZhiNan
 * @date : 2020/11/25 4:13 PM
 */
public class MKUtil {
    /**
     * 初始化
     * 采取全局初始化
     * @param context 最好使用Application
     */
    public static void initMMKV(Context context) {
        MMKV.initialize(context);
    }

    private static MMKV getMk(String fileName) {
        return MMKV.mmkvWithID(fileName);
    }

    private static boolean isNull(String fileName) {
        return TextUtils.isEmpty(fileName);
    }

    /**
      * @param fileName 文件名称
     * @param k=键
     * @param v=值
     */
    public static void setValue(String fileName, String k, String v) {
        if (isNull(fileName)) return;
        MMKV mk = getMk(fileName);
        if (mk != null) {
            mk.putString(k, v);
        }
    }

    /**
     * @param fileName 文件名
     * @param k=键
     * @param def=默认值
     * @return
     */
    public static String getValue(String fileName, String k, String def) {
        if (isNull(fileName)) return def;
        return getMk(fileName) != null ? getMk(fileName).getString(k, def) : def;
    }

    /**
     * @param fileName 文件名
     * @param k=键
     * @return 默认值返回""
     */
    public static String getValue(String fileName, String k) {
        return getValue(fileName, k, "");
    }

    /**
     * int类型存储方法
     * @param fileName 文件名
     * @param k=键
     * @param v=值
     */
    public static void setValue(String fileName, String k, int v) {
        if (isNull(fileName)) return;
        MMKV mk = getMk(fileName);
        if (mk != null) {
            mk.putInt(k, v);
        }
    }

    /**
     * 获取int类型的值
     *
      * @param fileName 文件名
     * @param k=键
     * @param def=默认值
     * @return
     */
    public static int getIntValue(String fileName, String k, int def) {
        if (isNull(fileName)) return 0;
        return getMk(fileName) != null ? getMk(fileName).getInt(k, def) : def;
    }

    /**
     * 获取int类型的值
     *
     * @param fileName 文件名
     * @param k=键
     * @return
     */
    public static int getIntValue(String fileName, String k) {
        return getIntValue(fileName, k, -1);
    }

    /**
     * 存boolean类型的值
     *
     * @param fileName 文件名
     * @param k=键
     * @param v
     */
    public static void setValue(String fileName, String k, boolean v) {
        if (isNull(fileName)) return;
        MMKV mk = getMk(fileName);
        if (mk != null) {
            mk.putBoolean(k, v);
        }
    }

    /**
     * 获取boolean类型的值
     *
     * @param fileName 文件名
     * @param k=键
     * @param def=默认值
     * @return
     */
    public static boolean getBooleanValue(String fileName, String k, boolean def) {
        if (isNull(fileName)) return def;
        MMKV mk = getMk(fileName);
        if (mk != null) {
            return mk.getBoolean(k, def);
        }
        return def;
    }

    /**
     * 无默认值获取boolean类型的值
     *
     * @param fileName 文件名
     * @param k=键
     * @return
     */
    public static boolean getBooleanValue(String fileName, String k) {
        return getBooleanValue(fileName, k, false);
    }

    /**
     * 储存float类型的值
     *
     * @param fileName 文件名称
     * @param k=键
     * @param v=值
     */
    public static void setValue(String fileName, String k, float v) {
        if (isNull(fileName)) return;
        MMKV mk = getMk(fileName);
        if (mk != null) {
            mk.putFloat(k, v);
        }
    }

    /**
     * 获取float类型的值
     *
     * @param fileName 文件名
     * @param k=键
     * @param def=默认值
     * @return
     */
    public static float getFloatValue(String fileName, String k, float def) {
        if (fileName == null) return def;
        MMKV mmkv = getMk(fileName);
        if (mmkv != null) {
           return mmkv.getFloat(k, def);
        }
        return def;
    }

    /**
     * 获取float类型的值，不需要设置默认值
     * @param fileName 文件名
     * @param k=键
     * @return
     */
    public static float getFloatValue(String fileName, String k) {
        return getFloatValue(fileName, k, -1f);
    }

    /**
     * 存储long类型的值
     *
     * @param fileName 文件名
     * @param k=键
     * @param v
     */
    public static void setValue(String fileName, String k, long v) {
        if (isNull(fileName)) return;
        MMKV mk = getMk(fileName);
        if (mk != null) {
            mk.putLong(k, v);
        }
    }

    /**
     * 获取long类型的值
     *
     * @param fileName 文件名
     * @param k=键
     * @param def=默认值
     * @return
     */
    public static long getLongValue(String fileName, String k, long def) {
        if (isNull(fileName)) return def;
        MMKV mk = getMk(fileName);
        if (mk != null) {
            return mk.getLong(k, def);
        }
        return def;
    }

    /**
     * 获取long类型的值，不需要设置默认值
     *
     * @param fileName 文件名
     * @param k=键
     * @return
     */
    public static long getLongValue(String fileName, String k) {
        return getLongValue(fileName, k, -1l);
    }

    /**
     * 存储对象
     * @param fileName=文件名称
     * @param k=键
     * @param t=对象类型
     * @param <T>
     */
    public static <T extends Parcelable> void setObject(String fileName, String k, T t) {
        try {
            String s = JSON.toJSONString(t);
            setValue(fileName, k, s);
        }catch (Exception e){
         }

    }

    /**
     * 获取对象
     * @param fileName=文件名称
     * @param k=键
     * @param t=类型
     * @param <T>
     * @return 返回对象
     */
    @Nullable
    public static <T> T getObject(String fileName, String k, Type t) {
        try {
            String s = getValue(fileName, k);
            if(TextUtils.isEmpty(s))return null;
            return (T) JSON.parseObject(s,t);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 删除一个文件下的其中一个key
     * @param fileName 文件名
     * @param k=键
     */
    public static void deleteValueForKey(String fileName,String k){
        if(isNull(fileName))return;
        MMKV mmkv=getMk(fileName);
        if(mmkv!=null){
            mmkv.removeValueForKey(k);
        }
    }

    /**
     * 清理文件
     * @param fileName
     */
    public static void clear(String fileName){
        if(isNull(fileName))return;
        MMKV mmkv=getMk(fileName);
        if(mmkv!=null){
            mmkv.clear();
        }
    }

}
