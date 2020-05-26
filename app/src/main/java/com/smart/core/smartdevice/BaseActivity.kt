package com.smart.core.smartdevice

import android.content.ComponentName
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

/**
 *
 * @author 王强 on 2019/2/26 249346528@qq.com
 */
abstract class BaseActivity : AppCompatActivity(), ServiceConnection {
    protected val permissionRequestCode = 0x02
    protected var penServiceConnected: Boolean = false

    /**
     * return true has permission in [permissions], false otherwise.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    protected fun checkAndRequestRecordPermission(requestCode: Int, vararg permissions: String): Boolean {
        val unPermission = permissions.filter {
            hasPermission(it).not()
        }
        if (unPermission.isNotEmpty()) {
            requestPermissions(unPermission.toTypedArray(), requestCode)
        }
        return unPermission.isEmpty()
    }

    /**
     * return true have permission
     */
    private fun hasPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    override fun onServiceDisconnected(name: ComponentName?) {
        penServiceConnected = false
        Toast.makeText(this@BaseActivity, "PenService destroyed.", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        penServiceConnected = true
    }
}