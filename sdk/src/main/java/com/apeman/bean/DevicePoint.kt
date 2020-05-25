package com.apeman.sdk.bean

import android.util.Log

/**
 * 带对象池
 * @author 王强 on 2019/3/14 249346528@qq.com
 */
class DevicePoint {
    /**
     * 0x00 -- 离开
     * 0x10 -- 悬空
     * 0x11 -- 压下
     */
    var state: Byte = 0x00
    var x: Int = 0
    var y: Int = 0
    var pressure: Int = 0
    var next: DevicePoint? = null
    //1 in used
    private var sFlag = 1

    fun recycle() {
        if (sFlag == 0) {
            return
        }
        state = 0x00
        x = 0
        y = 0
        pressure = 0
        synchronized(sPoolSync) {
            if (sPoolSize < MAX_POOL_SIZE) {
                next = sPool
                sPool = this
                sPoolSize++
                sFlag = 0
            }
        }
    }

    override fun toString(): String {
        return "DevicePoint(state=$state, x=$x, y=$y, pressure=$pressure, sPoolSize=$sPoolSize)"
    }


    companion object {
        private val sPoolSync = Any()
        private var sPoolSize = 0
        private const val MAX_POOL_SIZE = 50
        private var sPool: DevicePoint? = null
        fun obtain(): DevicePoint {
            Log.i("DevicePoint", "sPoolSize = $sPoolSize")
            synchronized(sPoolSync) {
                sPool?.apply {
                    val p = this
                    p.sFlag = 1
                    sPool = p.next
                    p.next = null
                    sPoolSize--
                    return p
                }
            }
            return DevicePoint()
        }
    }
}