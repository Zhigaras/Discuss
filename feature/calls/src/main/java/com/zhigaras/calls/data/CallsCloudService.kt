package com.zhigaras.calls.data

import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.cloudeservice.CloudService.Companion.CONNECTION_EVENT_PATH
import com.zhigaras.cloudeservice.CloudService.Companion.USERS_PATH

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
}