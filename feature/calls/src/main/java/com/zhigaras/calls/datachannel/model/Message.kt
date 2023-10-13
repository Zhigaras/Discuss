package com.zhigaras.calls.datachannel.model

abstract class Message {
    abstract val text: String
    abstract val type: MessageType
    
    class Incoming(override val text: String) : Message() {
        override val type = MessageType.INCOMING
    }
    
    class Outgoing(override val text: String) : Message() {
        override val type = MessageType.OUTGOING
    }
}

enum class MessageType {
    INCOMING, OUTGOING
}