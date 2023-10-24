package com.zhigaras.calls.domain.model

import android.util.Log
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
            Log.d("QQQWW", "offer")
            controller.sendAnswer(offer, sender, target)
            return
        }
        if (answer != null) {
            Log.d("QQQWW", "answer")
            controller.handleAnswer(answer)
            return
        }
        if (iceCandidate != null) {
            Log.d("QQQWW", "iceCandidate")
            controller.handleIceCandidate(iceCandidate)
            return
        }
    }
}

interface HandleConnectionData {
    
    fun handle(controller: CallsController)
}