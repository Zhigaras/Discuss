package com.zhigaras.calls.domain.model

import android.util.Log
import com.zhigaras.calls.domain.CallsController

data class ConnectionData(
    val opponent: ReadyToCallUser = ReadyToCallUser(),
    val interruptedByOpponent: Boolean = false,
    val offer: MySessionDescription? = null,
    val answer: MySessionDescription? = null,
    val iceCandidate: MyIceCandidate? = null
) : HandleConnectionData {
    override fun handle(controller: CallsController) {
        
        if (offer != null) {
            Log.d("QQQWW", "offer")
            controller.handleOffer(offer, opponent)
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
        if (interruptedByOpponent) {
            Log.d("QQQWW", "isCanceledByOpponent")
            controller.onConnectionInterruptedByOpponent()
        }
    }
}

interface HandleConnectionData {
    
    fun handle(controller: CallsController)
}