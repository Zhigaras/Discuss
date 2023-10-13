package com.zhigaras.messaging.domain

interface Messaging : DataChannelCommunication.Observe {
    
    fun sendMessage(text: String)
    
}