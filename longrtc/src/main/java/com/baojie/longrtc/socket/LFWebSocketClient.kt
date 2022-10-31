package com.baojie.longrtc.socket

import android.annotation.SuppressLint
import com.alibaba.fastjson.JSON
import com.baojie.baselib.utils.LifeScopeUtils
import com.baojie.longrtc.bean.MsgType
import com.baojie.longrtc.bean.SocketMsg
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

/**
 * @Description:
 * @Author baoJie
 * @Date 2022/10/31 16:16
 */
class LFWebSocketClient(serverUri: URI, private val event: ISocketEvent): WebSocketClient(serverUri) {

    private var connectFlag = false


    override fun onOpen(handshakedata: ServerHandshake?) {
        LogUtils.dTag(SocketManager.TAG, "onOpen")
        event.onOpen()
        connectFlag = true
    }

    override fun onMessage(message: String?) {
        LogUtils.dTag(SocketManager.TAG, "onMessage", message)
        message ?: return
        handleMessage(message)
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        LogUtils.dTag(SocketManager.TAG, "onClose: reason: $reason remote: $remote")
        if (connectFlag){
            //重连
            LifeScopeUtils.getLifeScope().launch {
                delay(3000)
                event.reConnect()
            }
        }else{
            event.logout("onClose")
        }
    }

    override fun onError(ex: Exception?) {
        LogUtils.dTag(SocketManager.TAG, "onError: ${ex.toString()}")
        event.logout("onError")
        connectFlag = false
    }

    // ---------------------------------------处理接收消息-------------------------------------

    private fun handleMessage(message: String) {
        val socketMsg = JSON.parseObject(message, SocketMsg::class.java)
        socketMsg ?: return
        socketMsg.eventName ?: return
        when(socketMsg.eventName){
            MsgType.LOGIN_SUCCESS -> {
                handleLogin(socketMsg)
            }
            MsgType.INVITE -> {
                handleInvite(socketMsg)
            }
            MsgType.CANCEL_CALL -> {
                handleCancel(socketMsg)
            }
            MsgType.START_RING -> {
                handleRing(socketMsg)
            }
            MsgType.JOIN -> {
                handleJoin(socketMsg)
            }
            MsgType.OTHER_JOIN -> {
                handleOtherJoin(socketMsg)
            }
            MsgType.REJECT_CALL -> {
                handleRejectCall(socketMsg)
            }
            MsgType.OFFER -> {
                handleOffer(socketMsg)
            }
            MsgType.ANSWER -> {
                handleAnswer(socketMsg)
            }
            MsgType.ICE_CANDIDATE -> {
                handleIceCandidate(socketMsg)
            }
            MsgType.LEAVE -> {
                handleLeave(socketMsg)
            }
            MsgType.CHANGE_AUDIO -> {
                handleChangeAudio(socketMsg)
            }
            MsgType.DISCONNECT -> {
                handleDisconnect(socketMsg)
            }
        }
    }

    private fun handleDisconnect(socketMsg: SocketMsg) {

    }

    private fun handleChangeAudio(socketMsg: SocketMsg) {

    }

    private fun handleLeave(socketMsg: SocketMsg) {

    }

    private fun handleIceCandidate(socketMsg: SocketMsg) {

    }

    private fun handleAnswer(socketMsg: SocketMsg) {

    }

    private fun handleOffer(socketMsg: SocketMsg) {

    }

    private fun handleRejectCall(socketMsg: SocketMsg) {

    }

    private fun handleOtherJoin(socketMsg: SocketMsg) {

    }

    private fun handleJoin(socketMsg: SocketMsg) {

    }

    private fun handleRing(socketMsg: SocketMsg) {

    }

    private fun handleCancel(socketMsg: SocketMsg) {

    }

    private fun handleInvite(socketMsg: SocketMsg) {

    }

    private fun handleLogin(socketMsg: SocketMsg) {
        val socketData = socketMsg.data
        socketData ?: return
        val userId = socketData.userID ?: ""
        val avatar = socketData.avatar ?: ""
        event.loginSuccess(userId, avatar)
    }

    @SuppressLint("CustomX509TrustManager")
    class TrustManagerTest: X509TrustManager{
        @SuppressLint("TrustAllX509TrustManager")
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

        }

        @SuppressLint("TrustAllX509TrustManager")
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {

        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }

    }
}