package com.baojie.longrtc.activity

import android.os.Bundle
import android.text.TextUtils
import com.baojie.longrtc.MainActivity
import com.baojie.longrtc.base.BaseActivity
import com.baojie.longrtc.cache.UserInfoUtils
import com.baojie.longrtc.constants.USER_DEFAULT_NAME
import com.baojie.longrtc.constants.WSS_URL
import com.baojie.longrtc.databinding.ActivityConfigBinding
import com.baojie.longrtc.socket.IUserState
import com.baojie.longrtc.socket.SocketManager
import com.baojie.longrtc.socket.UserState
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils

class ConfigActivity : BaseActivity(), IUserState {

    private lateinit var binding: ActivityConfigBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (SocketManager.getUserState() == UserState.LOGIN){
            ActivityUtils.startActivity(MainActivity::class.java)
            finish()
        }

        binding.etUrl.setText(WSS_URL)
        binding.etName.setText(USER_DEFAULT_NAME)

        initClick()
    }

    private fun initClick() {
        binding.btnConnect.setOnClickListener {
            val wsUrl = binding.etUrl.text.toString()
            if (TextUtils.isEmpty(wsUrl)){
                ToastUtils.showShort("请输入信令url")
                return@setOnClickListener
            }
            val userName = binding.etName.text.toString()
            if (TextUtils.isEmpty(userName)){
                ToastUtils.showShort("请输入用户名")
                return@setOnClickListener
            }
            // 设置用户名
            UserInfoUtils.setUserName(userName)
            // 添加登录回调
            SocketManager.addUserStateCallback(this)
            // 连接socket:登录
            SocketManager.connect(wsUrl, userName, 0)
        }
    }

    override fun userLogin() {
        ActivityUtils.startActivity(MainActivity::class.java)
        finish()
    }

    override fun userLogout() {

    }
}