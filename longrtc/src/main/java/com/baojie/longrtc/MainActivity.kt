package com.baojie.longrtc

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.baojie.longrtc.activity.ConfigActivity
import com.baojie.longrtc.base.BaseActivity
import com.baojie.longrtc.databinding.ActivityMainBinding
import com.baojie.longrtc.socket.IUserState
import com.blankj.utilcode.util.ActivityUtils

class MainActivity : BaseActivity(), IUserState {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        binding.lifecycleOwner = this
    }


    override fun userLogin() {

    }

    override fun userLogout() {
        if (!this.isFinishing){
            ActivityUtils.startActivity(ConfigActivity::class.java)
            finish()
        }
    }
}