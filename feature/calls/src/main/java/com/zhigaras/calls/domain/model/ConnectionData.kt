package com.zhigaras.calls.domain.model

import com.zhigaras.calls.domain.CallsController

data class ConnectionData(
    val target: String = "",
    val sender: String = "",
    val offer: MySessionDescription? = null,
    val answer: MySessionDescription? = null,
    val iceCandidate: MyIceCandidate? = null
) : HandleConnectionData {
    override fun handle(controller: CallsController) {
        
        if (offer != null) {
            controller.sendAnswer(offer, sender, target)
            return
        }
        if (answer != null) {
            controller.handleAnswer(answer)
            return
        }
        if (iceCandidate != null) {
            controller.handleIceCandidate(iceCandidate)
            return
        }
    }
}

interface HandleConnectionData {
    
    fun handle(controller: CallsController)
}