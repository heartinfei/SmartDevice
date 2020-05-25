package com.apeman.sdk.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 *
 * @author 王强 on 2019/3/26 249346528@qq.com
 */
class DuplicateObserver<T> : MutableLiveData<T>() {

    var mVersion = START_VERSION
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, ObserverWrapper(observer, this))
    }

    override fun setValue(value: T) {
        mVersion++
        super.setValue(value)
    }

    override fun postValue(value: T) {
        mVersion++
        super.postValue(value)
    }

    companion object {
        const val START_VERSION = -1
    }

    private inner class ObserverWrapper<T>(val observer: Observer<in T>,
                                           val liveData: DuplicateObserver<T>) : Observer<T> {
        // 通过标志位过滤旧数据
        private var mLastVersion = liveData.mVersion

        override fun onChanged(t: T?) {
            if (mLastVersion >= liveData.mVersion) {
                return
            }
            mLastVersion = liveData.mVersion

            observer.onChanged(t)
        }
    }
}



