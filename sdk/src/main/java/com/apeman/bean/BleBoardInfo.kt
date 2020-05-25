package com.apeman.sdk.bean

import com.apeman.sdk.utils.bytes2Int

/**
 * 板子信息页面
 *
 * @author 王强 on 2019/3/13 249346528@qq.com
 */
class BleBoardInfo(val name: String, val type: BoardType) {
    /**
     * 电量
     */
    var battery: Int = -1

    /**
     * 离线笔记数据量
     */
    var offLineNoteCount: Int = -1

    var softVersion: String = "v"
    var softInt: Int = -1
    var hardVersion: String = "v"
    var hardInt: Int = -1

    var mac: String? = null

    fun setBattery(b: Byte) {
        battery = bytes2Int(b)
    }

    fun setOffLineCount(c: Byte) {
        offLineNoteCount = bytes2Int(c)
    }

    fun setVersionInfo(versionInfo: ByteArray) {
        softInt = bytes2Int(*versionInfo.take(4).toByteArray())
        var sh: Int = softInt shr 28
        var sm: Int = softInt shr 16 and 0x0fff
        var sl: Int = softInt and 0xffff
        softVersion = "$sh.$sm.$sl"

        hardInt = bytes2Int(versionInfo[4], versionInfo[5])
        hardVersion = hardInt.toString()
    }

    override fun toString(): String {
        return "BleBoardInfo(battery=$battery, offLineNoteCount=$offLineNoteCount, softVersion='$softVersion', softInt=$softInt, hardVersion='$hardVersion', hardInt=$hardInt)"
    }


}