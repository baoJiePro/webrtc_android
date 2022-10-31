package com.baojie.baselib.livebus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import java.lang.Exception

/**
 * @Description: LiveData事件总线
 * @Author baojie@qding.me
 * @Date 2021/11/24 2:39 下午
 */
object LiveDataBus {

    private val map = hashMapOf<String, MMutableLiveData<*>>()


    fun <T> with(key: String): MMutableLiveData<T> {
        if (!map.containsKey(key)){
            synchronized(this){
                if (!map.containsKey(key)){
                    val liveData = MMutableLiveData<T>()
                    map[key] = liveData
                }
            }
        }

        return map[key] as MMutableLiveData<T>
    }

    /**
     * 删除未使用的liveData
     */
    fun removeUnusedLiveData(){
        val iterator = map.iterator()
        while (iterator.hasNext()){
            val entry = iterator.next()
            if (!entry.value.hasObservers()){
                iterator.remove()
            }
        }
    }


    class MMutableLiveData<T>: MutableLiveData<T>() {

        fun observe(owner: LifecycleOwner, observer: Observer<T>, intercept: Boolean = true){
            super.observe(owner, observer)
            if (intercept){
                try {
                    LiveDataHook.hook(observer, this)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }

        override fun postValue(value: T) {
            super.postValue(value)
        }

        override fun setValue(value: T) {
            super.setValue(value)
        }

    }

}