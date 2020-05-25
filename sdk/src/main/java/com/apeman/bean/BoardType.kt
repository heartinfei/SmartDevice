package com.apeman.sdk.bean

/**
 * 设备类型枚举
 * [width]和[height] 的定义是将（0.0）点旋转到左上角进行定义的
 *
 * @author 王强 on 2019/3/14 249346528@qq.com
 */
enum class BoardType(val width: Int, val height: Int, var maxPressure: Int) {
    //竖屏右上角（0,0）
    A5(43600, 27400, 2048) {
        override fun getX(originX: Int, originY: Int): Int {
            return originX
        }

        override fun getY(originX: Int, originY: Int): Int {
            return originY
        }

        override fun getMaxX(): Double = width.toDouble()

        override fun getMaxY(): Double = height.toDouble()
    },

    //竖屏左下角(0,0)
    Polestar(14500, 20000, 1023) {
        override fun getX(originX: Int, originY: Int): Int {
            return originX
        }

        override fun getY(originX: Int, originY: Int): Int {
            return 20000 - originY
        }

        override fun getMaxX(): Double = width.toDouble()

        override fun getMaxY(): Double = height.toDouble()
    },

    NoteMaker(21000, 12400, 1024) {
        override fun getX(originX: Int, originY: Int): Int {
            return originY
        }

        override fun getY(originX: Int, originY: Int): Int {
            return originX
        }

        override fun getMaxX(): Double {
            return width.toDouble()
        }

        override fun getMaxY(): Double {
            return height.toDouble()
        }
    };


    /**
     * 转化成手机屏幕坐标 -- X
     */
    abstract fun getX(originX: Int, originY: Int): Int

    /**
     * 转化成手机屏幕坐标 -- Y
     */
    abstract fun getY(originX: Int, originY: Int): Int

    abstract fun getMaxX(): Double

    abstract fun getMaxY(): Double
}