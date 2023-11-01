package com.zhigaras.calls.domain.model

class Topic(
    val id: Int = 0,
    val nameRu: String = "",
    val supportList: Map<String, String> = emptyMap(),
    val againstList: Map<String, String> = emptyMap(),
) {
    fun hasOpponent(opinion: DisputeParty): Boolean =
        when (opinion) {
            DisputeParty.AGAINST -> supportList.isNotEmpty()
            DisputeParty.SUPPORT -> againstList.isNotEmpty()
        }
    
    fun getOpponentId(opinion: DisputeParty) =
        when (opinion) {
            DisputeParty.AGAINST -> supportList.keys.first()
            DisputeParty.SUPPORT -> againstList.keys.first()
        }
}
