package com.zhigaras.calls.webrtc

import com.zhigaras.calls.domain.model.ConnectionData

interface NewEventCallBack {
    
    fun onNewEventReceived(data: ConnectionData)
    
}