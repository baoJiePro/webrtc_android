package com.baojie.baselib.livebus;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import androidx.arch.core.internal.SafeIterableMap;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

/**
 * @Description :TODO 通过hook源码实现控制黏性事件
 * @Author :YangZhiNan
 * @date : 2020/11/26 5:48 PM
 */
public class LiveDataHook {
    /**
     * TODO 通过阅读源码得知黏性事件是通过mVersionValue来控制的
     * TODO 通过反射修改mVersionValue 达到不发送黏性事件的目的
     * @param observer
     * @param liveData
     * @param <T>
     */
    public static  <T> void hook(Observer<T> observer, LiveData liveData){
        Class<LiveData> liveDataClass= (Class<LiveData>) liveData.getClass().getSuperclass();
        try {
            Field field=  liveDataClass.getSuperclass().getDeclaredField("mObservers");
            field.setAccessible(true);
            SafeIterableMap mMap= (SafeIterableMap) field.get(liveData);
            //获取map的get方法
            Method get=mMap.getClass().getDeclaredMethod("get", Object.class);
            get.setAccessible(true);
            //反射调用，observer就是map里面的key
            Object obj=  get.invoke(mMap,observer);
            if(obj!=null){
                Object observerWrapper =((Map.Entry)obj).getValue();
                Field mLastVersionField= observerWrapper.getClass().getSuperclass().getDeclaredField("mLastVersion");
                mLastVersionField.setAccessible(true);
                Field mVersionField=  liveDataClass.getSuperclass().getDeclaredField("mVersion");
                mVersionField.setAccessible(true);
                //liveData中的mVersion知道
                Object mVersionValue=mVersionField.get(liveData);
                //把mVersion中的值设置给mLastVersion
                mLastVersionField.set(observerWrapper,mVersionValue);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}

