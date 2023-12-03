package com.zhigaras.home.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class Permissions {
    
    private val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    
    fun check(context: Context, doOnGranted: () -> Unit, doOnDenied: (Array<String>) -> Unit) {
        val isAllGranted = permissions.all { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (isAllGranted) doOnGranted.invoke() else doOnDenied.invoke(permissions)
    }
}