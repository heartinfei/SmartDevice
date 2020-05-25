package com.apeman.sdk.bean

import java.util.*

/**
 * 存储一条笔迹将来会写入到文件
 *
 * @author 王强 on 2019/3/14 249346528@qq.com
 */
class DevicePath {
    /**
     * 三个点一组(x,y,p)
     */
    private var points = Collections.synchronizedList(mutableListOf<Int>())

    var lastPoint: DevicePoint? = null

    fun clear(block: ((String) -> Unit)?) {
        lastPoint?.apply {
            this.recycle()
            lastPoint = null

        }
        if (block != null) {
            block(points.toString())
        }
        points.clear()
    }

    fun addPoint(p: DevicePoint) {
        lastPoint?.recycle()
        lastPoint = p
        points.add(p.x)
        points.add(p.y)
        points.add(p.pressure)
    }


    fun distance2Path(p: DevicePoint): Int = lastPoint?.run {
        calculateDistance(this, p)
    } ?: 0

    private fun calculateDistance(p1: DevicePoint, p2: DevicePoint): Int {
        val dx = Math.abs(p1.x - p2.x).toDouble()
        val dy = Math.abs(p1.y - p2.y).toDouble()
        return Math.sqrt(dx * dx + dy * dy).toInt()
    }
}