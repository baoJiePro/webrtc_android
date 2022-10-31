package com.baojie.longrtc.bean

/**
 * @Description:
 * @Author baoJie
 * @Date 2022/10/31 17:23
 */

data class SocketMsg(
    var eventName: String? = null,
    var data: SocketData? = null
)

data class SocketData(
    var userID: String? = null,
    var avatar: String? = null
)

object MsgType{
    //登录成功
    const val LOGIN_SUCCESS = "login_success"
    //被邀请
    const val INVITE = "invite"
    //取消拨打
    const val CANCEL_CALL = "cancel_call"
    //响铃
    const val START_RING = "start_ring"
    //加入房间
    const val JOIN = "join"
    //其他人加入房间
    const val OTHER_JOIN = "other_join"
    //拒绝接听
    const val REJECT_CALL = "reject_call"
    //发送offer
    const val OFFER = "offer"
    //发送answer
    const val ANSWER = "answer"
    //ice-candidate
    const val ICE_CANDIDATE = "ice_candidate"
    //离开房间
    const val LEAVE = "leave"
    //切换语音
    const val CHANGE_AUDIO = "change_audio"
    //意外断开
    const val DISCONNECT = "disconnect"
}