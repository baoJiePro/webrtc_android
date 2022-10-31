package com.baojie.baselib.livebus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * @Description :TODO LiveBus类似EvenBus事件总线，基于LiveData的封装
 * TODO 主要特点有下面几点
 * TODO 1.能感知activity的生命周期，只有在可见状态才分发事件
 * TODO 2.在子线程里面使用postValue方法发送事件的时候,如果发送多长会覆盖,接收方只会接收到最后一次
 * TODO 3.通过反射hook源码，实现可以不接收黏性事件
 * TODO 4.黏性事件只有在生命周期处于可见状态才会收到
 * @Author :YangZhiNan
 * @date : 2020/11/26 4:51 PM
 */
public class LiveBus {
    private Object obj = new Object();
    private static volatile LiveBus instance = null;
    private Map<String, MutableLiveData<Object>> map;

    public static LiveBus lB() {
        if (instance == null) {
            synchronized (LiveBus.class) {
                if (instance == null) {
                    instance = new LiveBus();
                }
            }
        }
        return instance;
    }

    private LiveBus() {
        map = new HashMap<>();
    }

    /**
     * 生产liveData对象
     *
     * @param key 对象名称
     * @param cl  泛型类
     * @param <T>
     * @return
     */
    public <T> MMutableLiveData<T> with(String key, Class<T> cl) {
        MMutableLiveData mutableLiveData = null;
        if (!map.containsKey(key)) {
            synchronized (obj) {
                if (!map.containsKey(key)) {
                    mutableLiveData = new MMutableLiveData<T>();
                    map.put(key, mutableLiveData);
                }
            }
        }
        return mutableLiveData == null ? (MMutableLiveData<T>) map.get(key) : mutableLiveData;
    }

    /**
     * 自定义liveData扩展，可以不适用粘性通知
     *
     * @param <T>
     */
    public static class MMutableLiveData<T> extends MutableLiveData<T> {
        public MMutableLiveData() {
            super();
        }

        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer, boolean intercept) {
            super.observe(owner, observer);
            if (intercept) {
                try {
                    LiveDataHook.hook(observer, this);
                } catch (Exception e) {

                }
            }
        }

        /**
         * 在子线程中发送消息，使用这个方法
         * 如果多次发送接收方只会受到最后一个
         * @param value
         */
        @Override
        public void postValue(final T value) {
            super.postValue(value);
        }

        /**
         * 在UI线程发送消息使用这个方法
         * @param value
         */
        @Override
        public void setValue(T value) {
            super.setValue(value);
        }
    }
    public Map<String, MutableLiveData<Object>> getMap() {
        return map;
    }
    /**
     * 移除未使用的liveData
     */
    public static void removeUnusedLiveData(){
        try {
            final Map<String, MutableLiveData<Object>> map = LiveBus.lB().getMap();
            Iterator<Map.Entry<String, MutableLiveData<Object>>> it = map.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<String, MutableLiveData<Object>> entry = it.next();
                MutableLiveData<Object> mutableLiveData=entry.getValue();
                boolean hasObservers=  mutableLiveData.hasObservers();
                if(!hasObservers){
                    it.remove();
                }
            }
        }catch (Exception e){

        }
    }

}
