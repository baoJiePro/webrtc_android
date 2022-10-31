package com.baojie.longrtc.socket

import com.baojie.baselib.utils.LifeScopeUtils
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.URI
import java.net.URISyntaxException
import java.security.SecureRandom
import javax.net.ssl.SSLContext

/**
 * @Description:
 * @Author baoJie
 * @Date 2022/10/31 15:35
 */
object SocketManager : ISocketEvent {

    const val TAG = "SocketManager"

    private var userState: UserState = UserState.INIT
    private var iUserState: WeakReference<IUserState>? = null

    private var webSocket: LFWebSocketClient? = null

    private var myId: String = ""

    fun getUserState(): UserState {
        return userState
    }

    override fun connect(url: String, userId: String, device: Int) {
        if (webSocket == null || !webSocket!!.isOpen) {
            var uri: URI? = null
            try {
                // val uriUrl = "$url/$userId/$device"
                val uriUrl = url
                uri = URI(uriUrl)
            } catch (e: URISyntaxException) {
                e.printStackTrace()
                return
            }
            webSocket = LFWebSocketClient(uri, this)
            if (url.startsWith("wss")) {
                try {
                    val sslContext = SSLContext.getInstance("TLS")
                    sslContext?.init(
                        null,
                        arrayOf(LFWebSocketClient.TrustManagerTest()),
                        SecureRandom()
                    )
                    val factory = sslContext.socketFactory
                    factory?.let {
                        webSocket?.socket = it.createSocket()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            webSocket?.connect()

        }
    }

    override fun onOpen() {
        LogUtils.dTag(TAG, "socket is open!")
    }

    override fun loginSuccess(userId: String, avatar: String) {
        LogUtils.dTag(TAG, "loginSuccess: $userId")
        myId = userId
        userState = UserState.LOGIN
        //登录成功
        iUserState?.get()?.userLogin()
    }

    override fun onInvite(room: String, audioOnly: Boolean, inviteId: String, userList: String) {
        TODO("Not yet implemented")
    }

    override fun onCancel(inviteId: String) {
        TODO("Not yet implemented")
    }

    override fun onRing(userId: String) {
        TODO("Not yet implemented")
    }

    override fun onPeers(myId: String, userList: String, roomSize: Int) {
        TODO("Not yet implemented")
    }

    override fun onNewPeer(myId: String) {
        TODO("Not yet implemented")
    }

    override fun onReject(userId: String, type: Int) {
        TODO("Not yet implemented")
    }

    override fun onOffer(userId: String, sdp: String) {
        TODO("Not yet implemented")
    }

    override fun onAnswer(userId: String, sdp: String) {
        TODO("Not yet implemented")
    }

    override fun onIceCandidate(userId: String, id: String, label: Int, candidate: String) {
        TODO("Not yet implemented")
    }

    override fun onLeave(userId: String) {
        TODO("Not yet implemented")
    }

    override fun logout(str: String) {
        LogUtils.dTag(TAG, "logout: $str")
        userState = UserState.LOGOUT
        //登出回调
        iUserState?.get()?.userLogout()
    }

    override fun onTransAudio(userId: String) {
        TODO("Not yet implemented")
    }

    override fun onDisConnect(userId: String) {
        TODO("Not yet implemented")
    }

    override fun reConnect() {
        //重连
        LifeScopeUtils.getLifeScope().launch(Dispatchers.Main) {
            webSocket?.reconnect()
        }
    }

    fun addUserStateCallback(userState: IUserState) {
        iUserState = WeakReference<IUserState>(userState)
    }
}