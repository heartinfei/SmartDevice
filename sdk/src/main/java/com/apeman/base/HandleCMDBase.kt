package com.apeman.sdk.base

import com.apeman.sdk.service.PenPresentation

/**
 *
 * @author 王强 on 2019/3/13 249346528@qq.com
 */
abstract class HandleCMDBase(protected val penPresentation: PenPresentation) {
    var nextHandler: HandleCMDBase? = null
    abstract fun handle(data: ByteArray)
    protected fun getPayload(data: ByteArray): ByteArray? = if (data.size > 3) {
        data.copyOfRange(3, data.size)
    } else {
        null
    }
}