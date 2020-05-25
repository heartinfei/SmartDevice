package com.apeman.sdk.bean

import android.os.Parcel
import android.os.Parcelable
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * 笔记文件的描述
 * Note-1552888713217.note
 */
class NoteDescription() : Parcelable {
    /**
     * 文件正则
     */

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.CHINA)

    var timeStamp = System.currentTimeMillis()

    lateinit var notesDir: File

    lateinit var noteFile: File

    constructor(f: File) : this() {
        this.noteFile = f
        this.timeStamp = getTimeStampFromPath(noteFile.path)
        this.notesDir = noteFile.parentFile
    }

    constructor(stamp: Long, dir: File) : this() {
        this.timeStamp = stamp
        this.notesDir = dir
        this.noteFile = File(notesDir, "Note-$timeStamp.note")
    }

    constructor(source: Parcel) : this((File(source.readString())))

    /**
     * 从文件路径中获取时间戳信息
     */
    private fun getTimeStampFromPath(path: String): Long {
        val matchResult = NOTE_FILE_PATTERN.matcher(path)
        return if (matchResult.find()) {
            matchResult.group(1).toLong()
        } else {
            -1
        }
    }

    fun getNotFileName(): String {
        return noteFile.name
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(noteFile.path)
    }

    companion object {
        val NOTE_FILE_PATTERN = Pattern.compile("Note-(\\d{13})\\.note")

        @JvmField
        val CREATOR: Parcelable.Creator<NoteDescription> =
            object : Parcelable.Creator<NoteDescription> {
                override fun createFromParcel(source: Parcel): NoteDescription =
                    NoteDescription(source)

                override fun newArray(size: Int): Array<NoteDescription?> = arrayOfNulls(size)
            }
    }
}