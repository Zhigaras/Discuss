package com.zhigaras.calls.ui

import android.view.View
import com.zhigaras.core.UiState
import com.zhigaras.webrtc.R
import com.zhigaras.webrtc.databinding.FragmentCallBinding
import org.webrtc.PeerConnection.PeerConnectionState

interface CallUiState : UiState<FragmentCallBinding> {
    
    class LookingForOpponent : CallUiState {
        
        override fun update(binding: FragmentCallBinding) {
            binding.interruptedView.visibility = View.GONE
            binding.waitingView.startSearch()
        }
    }
    
    class WaitingForOpponent : CallUiState {
        
        override fun update(binding: FragmentCallBinding) {
            binding.interruptedView.visibility = View.GONE
            binding.waitingView.startWaiting()
        }
    }
    
    abstract class Connection : CallUiState {
        
        protected abstract val connectionState: PeerConnectionState
        
        fun match(state: PeerConnectionState) = connectionState == state
    }
}

class New : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.NEW
    override fun update(binding: FragmentCallBinding) {
        binding.interruptedView.visibility = View.GONE
    }
}

class Connecting : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.CONNECTING
    override fun update(binding: FragmentCallBinding) {
        binding.interruptedView.visibility = View.GONE
        binding.waitingView.startConnecting()
    }
}

class Connected : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.CONNECTED
    
    override fun update(binding: FragmentCallBinding) {
        binding.interruptedView.visibility = View.GONE
        binding.waitingView.stop()
    }
}

class Disconnected : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.DISCONNECTED
    
    override fun update(binding: FragmentCallBinding) {
        binding.interruptedView.visibility = View.VISIBLE
        binding.interruptedView.setText(R.string.disconnected)
    }
}

class Failed : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.FAILED
    override fun update(binding: FragmentCallBinding) {
//        binding.interruptedView.visibility = View.VISIBLE
//        binding.interruptedView.setText(R.string.connection_failed)
    }
}

class Closed : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.CLOSED
    override fun update(binding: FragmentCallBinding) {
        binding.interruptedView.visibility = View.GONE
    }
}

class InterruptedByOpponent : CallUiState {
    
    override fun update(binding: FragmentCallBinding) {
        binding.interruptedView.visibility = View.VISIBLE
        binding.interruptedView.setText(R.string.interrupted_by_opponent)
    }
}

class CheckConnection : CallUiState {
    
    override fun update(binding: FragmentCallBinding) {
        binding.interruptedView.visibility = View.VISIBLE
        binding.interruptedView.setText(R.string.check_connection)
    }
}

class TryingToReconnect : CallUiState {
    
    override fun update(binding: FragmentCallBinding) {
        binding.interruptedView.visibility = View.GONE
        binding.waitingView.startReconnect()
        
    }
}