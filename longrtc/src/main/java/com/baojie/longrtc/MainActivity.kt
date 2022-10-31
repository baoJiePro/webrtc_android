package com.baojie.longrtc

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.baojie.longrtc.activity.ConfigActivity
import com.baojie.longrtc.base.BaseActivity
import com.baojie.longrtc.databinding.ActivityMainBinding
import com.baojie.longrtc.socket.IUserState
import com.baojie.longrtc.socket.SocketManager
import com.blankj.utilcode.util.ActivityUtils

class MainActivity : BaseActivity(), IUserState {
    private lateinit var binding: ActivityMainBinding
    private var isFromCall = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        initNavigation()
    }

    private fun initNavigation() {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_user,
            R.id.navigation_room,
            R.id.navigation_setting
        ).build()
        // 設置ActionBar跟随联动
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        // 设置Nav跟随联动
        NavigationUI.setupWithNavController(binding.navView, navController)
        SocketManager.addUserStateCallback(this)


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