package com.baojie.baselib.utils

import android.view.View

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2021/6/9 11:54 上午
 */

inline fun View.setOnSingleClickListener(delay: Long = 800, crossinline onClick: () -> Unit){
    this.setOnClickListener {
        this.isClickable = false
        onClick()
        this.postDelayed({
            this.isClickable = true
        }, delay)
    }
}