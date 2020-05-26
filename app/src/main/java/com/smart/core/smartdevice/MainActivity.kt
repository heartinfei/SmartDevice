package com.smart.core.smartdevice

import android.app.ProgressDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.usb.UsbDevice
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import com.apeman.sdk.bean.BoardType
import com.apeman.sdk.bean.NoteDescription
import com.apeman.sdk.bean.UsbBoardInfo
import com.apeman.sdk.service.ConnectStatus
import com.apeman.sdk.service.PenService
import com.apeman.sdk.service.UsbPenService
import com.apeman.sdk.service.command.Command
import com.apeman.sdk.service.usb.USBPenServiceImpl
import com.apeman.sdk.widget.BoardViewCallback
import kotlinx.android.synthetic.main.activity_board.*

class MainActivity : BaseActivity() {
    private val cleanProgress: ProgressDialog by lazy {
        ProgressDialog(this).apply {
            setCancelable(false)
        }
    }
    private var pen: UsbPenService<UsbBoardInfo, UsbDevice>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        cbRestore.setOnClickListener {
            //重置操作
            boardView.setStrokeColor(Color.BLACK)
            boardView.setStrokeLevel(2)
        }

        btnPlayback.setOnClickListener {
            if (btnPlayback.text == getString(R.string.playback)) {
                btnPlayback.text = getString(R.string.stop_playback)
                boardView.startPlayBackCurrentNote {
                    btnPlayback.text = getString(R.string.playback)
                }
            } else {
                boardView.stopPlayBack()
                btnPlayback.text = getString(R.string.playback)
            }
        }

        btnRecord.setOnClickListener {
            if ((it as Button).text == getText(R.string.record_start)) {
                it.text = getString(R.string.record_stop)
                boardView.startRecord()
            } else {
                btnRecord.isEnabled = false
                boardView.stopRecord {
                    it.text = getString(R.string.record_start)
                    btnRecord.isEnabled = true
                }
            }
        }

        boardView.post {
            //关于当前连接的硬件类型，目前这里是写死的，如果将来兼容多款硬件需要动态获取当前连接设备的类型
            intent.getParcelableExtra<NoteDescription>("versionInfo")?.apply {
                boardView.setup(BoardType.NoteMaker, this)
            } ?: boardView.setup(BoardType.NoteMaker)

            boardView.loadFinishCallback = object : BoardViewCallback {
                override fun onLoadFinished() {
                    PenService.connectUsbService(this@MainActivity, this@MainActivity)
                }
            }
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        super.onServiceConnected(name, service)
        pen = (service as USBPenServiceImpl.ConnBinder).getService()
        //监听设备连接状态
        pen?.connLiveData?.observe(this, connObserver)
        //接收按键命令
        pen?.commandLiveData?.observe(this@MainActivity, cmdObserver)
        pen?.listConnectedUsbDevice(this@MainActivity)?.firstOrNull()?.apply {
            //连接到USB设备
            pen!!.connectSmartBoard(this)
        }
    }

    private val connObserver = object : Observer<ConnectStatus> {
        private var lastValue: ConnectStatus? = null
        override fun onChanged(it: ConnectStatus?) {
            if (lastValue == it) {
                return
            }
            lastValue = it
            if (it?.result == true) {
                //这里需要注意一定要在完成画板的初始化后再监听硬件的报点
                pen?.observeDevicePoint {
                    boardView.onPointReceived(it)
                }
            } else {
                Toast.makeText(this@MainActivity, it?.msg, Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private val cmdObserver = Observer<Command> {
        if (it == null) {
            return@Observer
        }
//        S.i(it.extra?.toHexString())
        when (it.extra) {
            0x00.toByte() -> { //清屏
                cleanBoardView()
            }

            0x01.toByte() -> { //保存
                boardView.newPage()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_board_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionClean -> {
                cleanBoardView()
                true
            }
            R.id.actionCreate -> {
                boardView.newPage()
                true
            }
            R.id.actionPlay -> {
                if (item.title == getString(R.string.playback)) {
                    item.title = getString(R.string.stop_playback)
                    boardView.startPlayBackCurrentNote {
                        item.title = getString(R.string.playback)
                    }
                } else {
                    boardView.stopPlayBack()
                    item.title = getString(R.string.playback)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cleanBoardView() {
        cleanProgress.show()
        boardView.clearBoardAndFile {
            if (cleanProgress.isShowing) {
                cleanProgress.dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (cleanProgress.isShowing) {
            cleanProgress.dismiss()
        }
        if (penServiceConnected) {
            PenService.disconnectUsbService(this, this)
        }
        pen?.apply {
            observeDevicePoint(null)
            commandLiveData.removeObserver(cmdObserver)
        }
    }

    companion object {
        /**
         * 启动BoardActivity
         */
        fun launch(ctx: Context, noteDescription: NoteDescription? = null) {
            Intent(ctx, MainActivity::class.java).apply {
                putExtra("versionInfo", noteDescription)
                ctx.startActivity(this)
            }
        }
    }
}