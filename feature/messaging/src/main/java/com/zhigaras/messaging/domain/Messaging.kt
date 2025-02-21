package com.zhigaras.messaging.domain

interface Messaging : DataChannelStateFlux.Observe {
    fun sendMessage(text: String)
}
