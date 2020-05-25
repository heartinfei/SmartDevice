package com.apeman.sdk.bean

import android.util.Log
import com.apeman.sdk.utils.bytes2Int
import java.util.*

/**
 *
 * @author 王强 on 2019/3/25 249346528@qq.com
 */
class SyncHeader(data: ByteArray) {
    val noteIdentifer = bytes2Int(data[0], data[1])
    val noteNumber = bytes2Int(data[2])
    val flashEraseFlag = bytes2Int(data[3])
    val noteStartSector = bytes2Int(data[4], data[5])
    val noteLen = bytes2Int(data[6], data[7], data[8], data[9])
    val year = bytes2Int(data[10]) + 2000
    val month = bytes2Int(data[11])
    val day = bytes2Int(data[12])
    val hour = bytes2Int(data[13])
    val min = bytes2Int(data[14])

    fun getTimeStamp(): String = Calendar.getInstance().apply {
        set(year, month, day, hour, min)
    }.timeInMillis.toString().apply {
        Log.i("SyncHeader","笔迹时间戳：$this")
    }

    override fun toString(): String {
        return "SyncHeader(noteIdentifer=$noteIdentifer, noteNumber=$noteNumber, flashEraseFlag=$flashEraseFlag, noteStartSector=$noteStartSector, noteLen=$noteLen, year=$year, month=$month, day=$day, hour=$hour, min=$min)"
    }


}