package com.zhigaras.calls.domain

import com.zhigaras.calls.domain.model.ReadyToCallUser
import com.zhigaras.calls.domain.model.Topic
import com.zhigaras.cloudservice.CloudService
import com.zhigaras.cloudservice.CloudService.Companion.TOPICS_PATH

interface MatchingInteractor {
    
    suspend fun addUserToWaitList(user: ReadyToCallUser)
    
    suspend fun checkMatching(user: ReadyToCallUser): MatchingResult
    
    class Base(private val cloudService: CloudService) : MatchingInteractor {
        
        override suspend fun addUserToWaitList(user: ReadyToCallUser) =
            user.addSelfToWaitList(cloudService)
        
        override suspend fun checkMatching(user: ReadyToCallUser): MatchingResult {
            return try {
                val topic = cloudService.getDataSnapshot(
                    Topic::class.java,
                    TOPICS_PATH,
                    user.topicId.toString()
                )
                if (topic.hasOpponent(user.disputeParty!!)) {
                    val opponentId = topic.getOpponentId(user.disputeParty)
                    MatchingResult.OpponentFound(
                        ReadyToCallUser(opponentId, user.topicId, user.disputeParty.opposite())
                    )
                } else {
                    MatchingResult.NoMatch(user)
                }
            } catch (e: Exception) {
                MatchingResult.Error(e.message ?: "Unknown error")
            }
        }
    }
}