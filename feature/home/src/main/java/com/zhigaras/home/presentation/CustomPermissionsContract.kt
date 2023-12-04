package com.zhigaras.home.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import com.zhigaras.calls.domain.model.DisputeParty
import com.zhigaras.home.R

class CustomPermissionsContract :
    ActivityResultContract<PermissionRequest, PermissionRequestResult>() {
    
    private lateinit var request: PermissionRequest
    
    override fun createIntent(context: Context, input: PermissionRequest): Intent {
        request = input
        return input.createIntent()
    }
    
    override fun parseResult(resultCode: Int, intent: Intent?): PermissionRequestResult {
        return request.createResult(resultCode, intent)
    }
}

class PermissionRequest(
    private val permissions: Array<String>,
    private val topicId: Int,
    private val opinion: DisputeParty
) {
    
    fun createIntent() = Intent(ACTION_REQUEST_PERMISSIONS).putExtra(EXTRA_PERMISSIONS, permissions)
    
    fun createResult(resultCode: Int, intent: Intent?): PermissionRequestResult {
        if (resultCode != Activity.RESULT_OK) return PermissionRequestResult.Empty()
        if (intent == null) return PermissionRequestResult.Empty()
        val permissions = intent.getStringArrayExtra(EXTRA_PERMISSIONS)
        val grantResults = intent.getIntArrayExtra(EXTRA_PERMISSION_GRANT_RESULTS)
        if (grantResults == null || permissions == null) return PermissionRequestResult.Empty()
        val grantState = grantResults.map { result ->
            result == PackageManager.PERMISSION_GRANTED
        }
        val grantedResult = permissions.filterNotNull().zip(grantState).toMap()
        return PermissionRequestResult.Base(grantedResult, topicId, opinion)
    }
    
    companion object {
        const val EXTRA_PERMISSIONS = "androidx.activity.result.contract.extra.PERMISSIONS"
        const val EXTRA_PERMISSION_GRANT_RESULTS =
            "androidx.activity.result.contract.extra.PERMISSION_GRANT_RESULTS"
        private const val ACTION_REQUEST_PERMISSIONS =
            "androidx.activity.result.contract.action.REQUEST_PERMISSIONS"
    }
}

interface PermissionRequestResult {
    
    fun handle(context: Context, viewModel: HomeViewModel)
    
    class Base(
        private val grantResult: Map<String, Boolean>,
        private val subId: Int,
        private val disputeParty: DisputeParty
    ) : PermissionRequestResult {
        override fun handle(context: Context, viewModel: HomeViewModel) {
            if (grantResult.values.all { it }) viewModel.navigateToCall(subId, disputeParty)
            else Toast.makeText(context, R.string.permissions_needed, Toast.LENGTH_SHORT).show()
        }
    }
    
    class Empty : PermissionRequestResult {
        override fun handle(context: Context, viewModel: HomeViewModel) = Unit
    }
}