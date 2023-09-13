package com.zhigaras.calls.webrtc

import com.zhigaras.calls.domain.model.DataModel

interface NewEventCallBack {
    
    fun onNewEventReceived(model: DataModel)
    
}