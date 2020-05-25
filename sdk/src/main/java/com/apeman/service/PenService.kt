package com.apeman.sdk.service

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.hardware.usb.UsbDevice
import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import com.apeman.sdk.base.DuplicateObserver
import com.apeman.sdk.bean.DevicePoint
import com.apeman.sdk.bean.SyncResult
import com.apeman.sdk.service.ble.BLEPenServiceImpl
import com.apeman.sdk.service.command.Command
import com.apeman.sdk.service.usb.USBPenServiceImpl

/**
 * 设备连接结果返回
 * @author 王强 on 2019/3/17 249346528@qq.com
 */
@Keep
class ConnectStatus(val result: Boolean = false, var msg: String? = null)

/**
 * USB设备连接服务
 * @author 王强 on 2019/2/26 249346528@qq.com
 */
@Keep
interface UsbPenService<BoardInfo, Device> : PenService<BoardInfo, Device> {
    /**
     * 搜索当前链接的设备
     * [ctx]
     */
    fun listConnectedUsbDevice(ctx: Context): List<UsbDevice>
}

/**
 * Ble设备连接服务
 * @author 王强 on 2019/2/26 249346528@qq.com
 */
@Keep
interface PenService<BoardInfo, Device> {

    /**
     * 接收链接结果,null 代表断开链接
     */
    val connLiveData: MutableLiveData<ConnectStatus>

    /**
     * 监听设备状态(电量、离线笔记数量、版本信息、Mac地址)
     */
    val boardInfoLiveData: MutableLiveData<BoardInfo>

    /**
     * 硬件发送的指令信息如：翻页，清屏等
     */
    val commandLiveData: MutableLiveData<Command>

    /**
     * 同步
     */
    var syncLiveData: MutableLiveData<SyncResult>

    /**
     * 当前链接设备的信息
     */
    var boardInfo: BoardInfo?

    /**
     * 建立BLE链接
     */
    fun connectSmartBoard(r: Device)

    /**
     * Return true 和Ble设备连接成功
     */
    fun isConnected(): Boolean

    /**
     * 断开连接
     */
    fun disconnectSmartBoard()

    /**
     * 同步离线笔迹
     */
    fun syncOffLineNotes()

    /**
     * 返回报点模式
     */
    fun resetDeviceMode()

    /**
     * 接收点数据
     */
    fun observeDevicePoint(observer: ((DevicePoint) -> Unit)?)

    /**
     * 发送命令
     */
    fun execCmd(cmd: Command, payload: ByteArray?)

    /**
     * 清屏
     */
    fun clean()

    /**
     * 翻页
     */
    fun nextPage()

    companion object {
        /**
         * 启动BLE笔服务
         * [ctx]
         */
        fun startBleService(ctx: Context) {
            Intent(ctx, BLEPenServiceImpl::class.java).apply {
                ctx.startService(this)
            }
        }

        /**
         * 停止BLE笔服务
         * [ctx]
         */
        fun stopBleService(ctx: Context) {
            ctx.stopService(Intent(ctx, BLEPenServiceImpl::class.java))
        }

        /**
         * 链接到BLE笔服务
         * [ctx]
         * [conn]
         */
        fun connectBleService(ctx: Context, conn: ServiceConnection) {
            Intent(ctx, BLEPenServiceImpl::class.java).apply {
                ctx.bindService(this, conn, Context.BIND_AUTO_CREATE)
            }
        }

        /**
         * 断开BLE笔服务
         * [ctx]
         * [conn]
         */
        fun disconnectBleService(ctx: Context, conn: ServiceConnection) {
            ctx.unbindService(conn)
        }

        /**
         * 启动USB笔服务
         * [ctx]
         */
        fun startUsbService(ctx: Context) {
            Intent(ctx, USBPenServiceImpl::class.java).apply {
                ctx.startService(this)
            }
        }

        /**
         * 链接到USB服务
         * [ctx]
         * [conn]
         */
        fun connectUsbService(ctx: Context, conn: ServiceConnection) {
            Intent(ctx, USBPenServiceImpl::class.java).apply {
                ctx.bindService(this, conn, Context.BIND_AUTO_CREATE)
            }
        }

        /**
         * 断开USB笔服务
         * [ctx]
         * [conn]
         */
        fun disconnectUsbService(ctx: Context, conn: ServiceConnection) {
            ctx.unbindService(conn)
        }

        /**
         * 停止USB服务
         * [ctx]
         */
        fun stopUsbService(ctx: Context) {
            ctx.stopService(Intent(ctx, USBPenServiceImpl::class.java))
        }
    }
}