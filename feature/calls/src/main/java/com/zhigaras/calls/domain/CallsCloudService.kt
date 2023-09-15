package com.zhigaras.calls.domain

import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.cloudeservice.CloudService

interface CallsCloudService {
    
    fun sendToCloud(data: ConnectionData)
    
    fun observeUpdates(userId: String, callback: CloudService.Callback<ConnectionData>)
    
    class Base(
        private val cloudService: CloudService
    ) : CallsCloudService {
        
        override fun sendToCloud(data: ConnectionData) {
            cloudService.postMultipleLevels(data, USERS_PATH, data.target, CONNECTION_EVENT_PATH)
        }
        
        override fun observeUpdates(
            userId: String,
            callback: CloudService.Callback<ConnectionData>
        ) {
            cloudService.subscribeMultipleLevels(
                callback,
                ConnectionData::class.java,
                USERS_PATH,
                userId,
                CONNECTION_EVENT_PATH
            )
        }
    }
    
    companion object {
        private const val USERS_PATH = "Users"
        private const val CONNECTION_EVENT_PATH = "connectionEvent"
    }
}