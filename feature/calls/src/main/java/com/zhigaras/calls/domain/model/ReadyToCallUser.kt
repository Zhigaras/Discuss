package com.zhigaras.calls.domain.model

import com.zhigaras.cloudeservice.CloudService

open class ReadyToCallUser(
    val id: String = "",
    val subjectId: String = "",
    val disputeParty: DisputeParty? = null
) {
    
    fun removeSelfFromWaitList(cloudService: CloudService) {
        cloudService.removeListItem(id, CloudService.SUBJECTS_PATH, subjectId, disputeParty!!.path)
    }
    
    fun addSelfToWaitList(cloudService: CloudService) {
        cloudService.addItemToList(id, CloudService.SUBJECTS_PATH, subjectId, disputeParty!!.path)
    }
}