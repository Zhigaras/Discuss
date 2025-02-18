package com.zhigaras.messaging.domain.model

import com.zhigaras.adapterdelegate.ListItem
import java.util.UUID

abstract class Message : ListItem {
    abstract val text: String
    abstract val type: MessageType
    private val uid: UUID = UUID.randomUUID()
    
    override fun itemType() = type.ordinal
    
    override fun areItemTheSame(newItem: ListItem): Boolean {
        if (newItem !is Message) return false
        return this.uid == newItem.uid
    }
    
    data class Incoming(override val text: String) : Message() {
        override val type = MessageType.INCOMING
    }
    
    data class Outgoing(override val text: String) : Message() {
        override val type = MessageType.OUTGOING
    }
}

enum class MessageType {
    INCOMING, OUTGOING
}