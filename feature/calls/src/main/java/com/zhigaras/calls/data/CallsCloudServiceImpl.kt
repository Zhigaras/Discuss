package com.zhigaras.calls.data

import com.zhigaras.calls.domain.CallsCloudService
import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.cloudeservice.CloudService

class CallsCloudServiceImpl(
    private val cloudService: CloudService
) : CallsCloudService {
    
    override fun sendToCloud(data: ConnectionData) {
        cloudService.postMultipleLevels(
            data,
            CloudService.USERS_PATH, data.target,
            CloudService.CONNECTION_EVENT_PATH
        )
    }
    
    override fun observeUpdates(
        userId: String,
        callback: CloudService.Callback<ConnectionData>
    ) {
        cloudService.subscribeMultipleLevels(
            callback,
            ConnectionData::class.java,
            CloudService.USERS_PATH,
            userId,
            CloudService.CONNECTION_EVENT_PATH
        )
    }
}