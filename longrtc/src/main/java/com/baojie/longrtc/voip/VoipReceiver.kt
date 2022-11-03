package com.baojie.longrtc.voip

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.baojie.longrtc.constants.ACTION_VOIP_RECEIVER
import com.blankj.utilcode.util.AppUtils

/**
 * @Description:
 * @Author baoJie
 * @Date 2022/11/2 08:37
 */
class VoipReceiver : BroadcastReceiver(){

    private val TAG = this.javaClass.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return
        val action = intent.action
        action ?: return
        when(action){
            ACTION_VOIP_RECEIVER -> {
                val room = intent.getStringExtra("room")
                val audioOnly = intent.getBooleanExtra("audioOnly", false)
                val inviteId = intent.getStringExtra("inviteId") ?: "123456"
                val inviteUserName = intent.getStringExtra("inviteUserName") ?: "p2pChat"
                val userList = intent.getStringExtra("userList")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    if (AppUtils.isAppForeground()){
                        onForegroundOrBeforeVersionO(
                            room,
                            userList,
                            inviteId,
                            audioOnly,
                            inviteUserName,
                            true
                        )
                    }else{
                        onBackgroundAfterVersionO(
                            room,
                            userList,
                            inviteId,
                            audioOnly,
                            inviteUserName
                        )
                    }
                }else{
                    onForegroundOrBeforeVersionO(
                        room,
                        userList,
                        inviteId,
                        audioOnly,
                        inviteUserName,
                        AppUtils.isAppForeground()
                    )
                }
            }
        }
    }

    private fun onBackgroundAfterVersionO(
        room: String?,
        userList: String?,
        inviteId: String,
        audioOnly: Boolean,
        inviteUserName: String
    ) {

    }

    private fun onForegroundOrBeforeVersionO(
        room: String?,
        userList: String?,
        inviteId: String,
        audioOnly: Boolean,
        inviteUserName: String,
        isFront: Boolean
    ) {

    }
}