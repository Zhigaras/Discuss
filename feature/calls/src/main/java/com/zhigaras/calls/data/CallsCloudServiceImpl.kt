package com.zhigaras.calls.data

import com.zhigaras.calls.domain.CallsCloudService
import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.calls.domain.model.ReadyToCallUser
import com.zhigaras.cloudeservice.CloudService

class CallsCloudServiceImpl(
    private val cloudService: CloudService
) : CallsCloudService {
    
    override fun sendToCloud(data: ConnectionData, opponentId: String) {
        cloudService.postMultipleLevels(
            data,
            CloudService.USERS_PATH,
            opponentId,
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
    
    override fun removeConnectionData(userId: String) {
        cloudService.postMultipleLevels(
            null,
            CloudService.USERS_PATH,
            userId,
            CloudService.CONNECTION_EVENT_PATH,
            "iceCandidate"
        )
    }
    
    override fun removeInterruptionFlag(userId: String) {
        cloudService.postMultipleLevels(
            false,
            CloudService.USERS_PATH,
            userId,
            CloudService.CONNECTION_EVENT_PATH,
            "interruptedByOpponent"
        )
    }
    
    override fun removeUserFromWaitList(
        opponent: ReadyToCallUser
    ) = opponent.removeSelfFromWaitList(cloudService)
}