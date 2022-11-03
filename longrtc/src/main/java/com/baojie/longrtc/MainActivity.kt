package com.baojie.longrtc

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.baojie.longrtc.activity.ConfigActivity
import com.baojie.longrtc.base.BaseActivity
import com.baojie.longrtc.constants.ACTION_VOIP_RECEIVER
import com.baojie.longrtc.databinding.ActivityMainBinding
import com.baojie.longrtc.socket.IUserState
import com.baojie.longrtc.socket.SocketManager
import com.baojie.longrtc.voip.VoipReceiver
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils

class MainActivity : BaseActivity(), IUserState {
    private lateinit var binding: ActivityMainBinding
    private var isFromCall = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initNavigation()

        initData()
    }

    private fun initData() {
        isFromCall = intent.getBooleanExtra("isFromCall", false)
        if (isFromCall){
            initCall()
        }
    }

    private fun initCall() {
        val voip = Intent()
        voip.putExtra("room", intent.getStringExtra("room"))
        voip.putExtra("audioOnly", intent.getBooleanExtra("audioOnly", false))
        voip.putExtra("inviteId", intent.getStringExtra("inviteId"))
        voip.putExtra("inviteUserName", intent.getStringExtra("inviteUserName"))
        voip.putExtra("userList", intent.getStringExtra("userList"))
        voip.action = ACTION_VOIP_RECEIVER
        voip.component = ComponentName(AppUtils.getAppPackageName(), VoipReceiver::javaClass.name)
        sendBroadcast(voip)
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