package com.zhigaras.calls.data

import com.zhigaras.calls.domain.CallsCloudService
import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.calls.domain.model.ReadyToCallUser
import com.zhigaras.cloudservice.CloudService

class CallsCloudServiceImpl(
    private val cloudService: CloudService
) : CallsCloudService {
    
    override fun sendToCloud(data: ConnectionData, opponentId: String) {
        cloudService.post(
            data,
            CloudService.USERS_PATH,
            opponentId,
            CallsCloudService.CONNECTION_DATA_PATH
        )
    }
    
    override fun observeUpdates(userId: String) = cloudService.subscribe(
        ConnectionData::class.java,
        CloudService.USERS_PATH,
        userId,
        CallsCloudService.CONNECTION_DATA_PATH
    )
    
    
    override fun removeConnectionData(userId: String) {
        cloudService.post(
            null,
            CloudService.USERS_PATH,
            userId,
            CallsCloudService.CONNECTION_DATA_PATH,
            "iceCandidate"
        )
    }
    
    override fun removeOpponent(userId: String) {
        cloudService.post(
            null,
            CloudService.USERS_PATH,
            userId,
            CallsCloudService.CONNECTION_DATA_PATH,
            CallsCloudService.OPPONENT_EVENT_PATH
        )
    }
    
    override fun removeInterruptionFlag(userId: String) {
        cloudService.post(
            false,
            CloudService.USERS_PATH,
            userId,
            CallsCloudService.CONNECTION_DATA_PATH,
            "interruptedByOpponent"
        )
    }
    
    override fun removeUserFromWaitList(
        opponent: ReadyToCallUser
    ) = opponent.removeSelfFromWaitList(cloudService)
}