package com.baojie.longrtc.socket

/**
 * @Description:
 * @Author baoJie
 * @Date 2022/10/31 13:58
 */
interface ISocketEvent {

    fun connect(url: String, userId: String, device: Int)

    fun onOpen()

    fun loginSuccess(userId: String, avatar: String)

    fun onInvite(room: String, audioOnly: Boolean, inviteId: String, userList: String)

    fun onCancel(inviteId: String)

    fun onRing(userId: String)

    fun onPeers(myId: String, userList: String, roomSize: Int)

    fun onNewPeer(myId: String)

    fun onReject(userId: String, type: Int)

    fun onOffer(userId: String, sdp: String)

    fun onAnswer(userId: String, sdp: String)

    fun onIceCandidate(userId: String, id: String, label: Int, candidate: String)

    fun onLeave(userId: String)

    fun logout(str: String)

    fun onTransAudio(userId: String)

    fun onDisConnect(userId: String)

    fun reConnect()

}