package com.zhigaras.calls.domain.model

class ConnectionData(
    val target: String = "",
    val sender: String = "",
    val data: String = "",
    val type: ConnectionDataType = ConnectionDataType.EMPTY
)

enum class ConnectionDataType {
    EMPTY, OFFER, ANSWER, ICE_CANDIDATE, START_CALL
}
