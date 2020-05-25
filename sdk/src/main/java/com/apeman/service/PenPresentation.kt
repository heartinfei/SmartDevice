package com.apeman.sdk.service

import androidx.annotation.Keep


/**
 * BLE设备发送过来的数据的处理结果的回调接口
 *
 * @author 王强 on 2019/3/13 249346528@qq.com
 */
@Keep
interface PenPresentation {
    /**
     * 报告设备的状态信息,这里主要是电量和离线笔记的数量
     */
    fun onReceiveCMD_80(battery: Byte, count: Byte, state: Int)

    /**
     * 报告设备的版本号等相关信息，这里主要是硬件的版本号和软件的版本号
     */
    fun onReceiveCMD_84(version: ByteArray)

    /**
     * 报告设备的Mac地址信息
     */
    fun onReceiveCMD_C0(mac: String)

    /**
     * 上报Ble设备的当前的坐标和压感数据
     */
    fun onReceivePenData(data: ByteArray)

    /**
     * 上报离线笔记数据（坐标&压感等）
     */
    fun onReceiveCMD_A4(data: ByteArray)

    /**
     * 报告硬件指令
     * @param payload
     * 对于BLE板子：指令 0x01 翻页；0x02 清屏
     * 对于USB板子：0x00表示页面刷新按键按下，用于清屏；0x01表示存储按键按下，用于保存当前页面。
     */
    fun onReceiveCommand(payload: Byte)
}