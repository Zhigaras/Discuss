package com.zhigaras.calls.webrtc

import com.zhigaras.calls.domain.CallCommunication
import com.zhigaras.calls.ui.Closed
import com.zhigaras.calls.ui.Connected
import com.zhigaras.calls.ui.Connecting
import com.zhigaras.calls.ui.Disconnected
import com.zhigaras.calls.ui.Failed
import com.zhigaras.calls.ui.InterruptedByOpponent
import com.zhigaras.calls.ui.New
import org.webrtc.PeerConnection.PeerConnectionState

class PeerConnectionCallback(
    private val communication: CallCommunication.Post
) : (PeerConnectionState) -> Unit {
    
    inner class Factory {
        private val states =
            listOf(New(), Connecting(), Connected(), Disconnected(), Failed(), Closed())
        
        fun state(newState: PeerConnectionState) = states.find { it.match(newState) }!!
    }
    
    override fun invoke(newState: PeerConnectionState) {
        val state = Factory().state(newState)
        communication.postBackground(state)
    }
    
    fun postInterrupted() = communication.postBackground(InterruptedByOpponent())
}