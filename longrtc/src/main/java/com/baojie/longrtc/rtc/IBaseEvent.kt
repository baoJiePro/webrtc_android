package com.baojie.longrtc.rtc

/**
 * @Description:
 * @Author baoJie
 * @Date 2022/10/31 13:47
 */
interface IBaseEvent {
    // 创建房间
    fun createRoom(room: String, roomSize: Int)

    fun sendInvite(room: String, userIds: List<String>, audioOnly: Boolean)

    fun sendRefuse(room: String, inviteId: String, refuseType: Int)

    fun sendTransAudio(toId: String)

    fun sendDisConnect(room: String, toId: String, isCrashed: Boolean)

    fun sendCancel(room: String, toId: List<String>)

    fun sendJoin(room: String)

    fun sendRingBack(targetId: String, room: String)

    fun sendLeave(room: String, userId: String)

    fun sendOffer(userId: String, sdp: String)

    fun sendAnswer(userId: String, sdp: String)

    fun sendIceCandidate(userId: String, id: String, label: Int, candidate: String)

    fun onRemoteRing()

    fun shouldStartRing(isComing: Boolean)

    fun shouldStopRing()

}