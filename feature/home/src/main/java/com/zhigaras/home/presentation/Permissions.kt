package com.zhigaras.home.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.zhigaras.calls.domain.model.DisputeParty

class Permissions {
    
    private val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    
    fun check(
        context: Context,
        launcher: ActivityResultLauncher<PermissionRequest>,
        viewModel: HomeViewModel,
        topicId: Int,
        opinion: DisputeParty
    ) {
        val isAllGranted = permissions.all { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (isAllGranted) viewModel.navigateToCall(topicId, opinion)
        else launcher.launch(PermissionRequest(permissions, topicId, opinion))
    }
}