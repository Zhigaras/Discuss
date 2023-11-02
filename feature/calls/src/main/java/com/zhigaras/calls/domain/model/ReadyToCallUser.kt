package com.zhigaras.calls.domain.model

import android.os.Parcelable
import com.zhigaras.cloudservice.CloudService
import kotlinx.parcelize.Parcelize

@Parcelize
class ReadyToCallUser(
    val id: String = "",
    val topicId: Int = 0,
    val disputeParty: DisputeParty? = null
) : Parcelable {
    
    fun removeSelfFromWaitList(cloudService: CloudService) {
        cloudService.removeListItem(
            id,
            CloudService.TOPICS_PATH,
            topicId.toString(),
            disputeParty!!.path
        )
    }
    
    fun addSelfToWaitList(cloudService: CloudService) {
        cloudService.addItemToList(
            id,
            CloudService.TOPICS_PATH,
            topicId.toString(),
            disputeParty!!.path
        )
    }
}