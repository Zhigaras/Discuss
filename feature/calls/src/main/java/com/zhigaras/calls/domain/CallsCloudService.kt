package com.zhigaras.calls.domain

import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.calls.domain.model.ReadyToCallUser
import com.zhigaras.cloudeservice.CloudService

interface CallsCloudService {
    
    fun sendToCloud(data: ConnectionData, opponentId: String)
    
    fun observeUpdates(userId: String, callback: CloudService.Callback<ConnectionData>)
    
    fun removeConnectionData(userId: String)
    
    fun removeInterruptionFlag(userId: String)
    
    fun removeUserFromWaitList(opponent: ReadyToCallUser)
}