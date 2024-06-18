package com.zhigaras.calls.ui

import android.view.View
import com.zhigaras.core.UiState
import com.zhigaras.webrtc.R
import com.zhigaras.webrtc.databinding.FragmentCallBinding
import org.webrtc.PeerConnection.PeerConnectionState

interface CallUiState : UiState<FragmentCallBinding> {
    
    class LookingForOpponent : CallUiState {
        
        override fun update(binding: FragmentCallBinding) {
            binding.remoteViewOverlay.visibility = View.VISIBLE
            binding.connectionStateView.hide()
            binding.waitingView.startSearch()
            binding.nextButton.isEnabled = false
        }
    }
    
    class WaitingForOpponent : CallUiState {
        
        override fun update(binding: FragmentCallBinding) {
            binding.remoteViewOverlay.visibility = View.VISIBLE
            binding.connectionStateView.hide()
            binding.waitingView.startWaiting()
            binding.nextButton.isEnabled = false
        }
    }
    
    class InterruptedByOpponent : CallUiState {
        
        override fun update(binding: FragmentCallBinding) {
            binding.remoteViewOverlay.visibility = View.VISIBLE
            binding.connectionStateView.show(R.string.interrupted_by_opponent)
            binding.nextButton.isEnabled = true
        }
    }
    
    class CheckConnection : CallUiState {
        
        override fun update(binding: FragmentCallBinding) {
            binding.remoteViewOverlay.visibility = View.VISIBLE
            binding.connectionStateView.show(R.string.check_connection)
            binding.nextButton.isEnabled = false
        }
    }
    
    class TryingToReconnect : CallUiState {
        
        override fun update(binding: FragmentCallBinding) {
            binding.remoteViewOverlay.visibility = View.VISIBLE
            binding.waitingView.startReconnect()
            binding.nextButton.isEnabled = true
        }
    }
    
    class Error(private val message: String) : CallUiState {
        
        override fun update(binding: FragmentCallBinding) {
            binding.remoteViewOverlay.visibility = View.VISIBLE
            binding.connectionStateView.show(R.string.connection_error, message)
            binding.nextButton.isEnabled = true
        }
    }
    
    abstract class Connection : CallUiState {
        
        protected abstract val connectionState: PeerConnectionState
        protected abstract val messageId: Int
        
        fun match(state: PeerConnectionState) = connectionState == state
        
        override fun update(binding: FragmentCallBinding) {
            binding.connectionStateView.show(messageId)
        }
    }
}

class New : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.NEW
    override val messageId = R.string.new_connection
    
}

class Connecting : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.CONNECTING
    override val messageId = R.string.connecting
    
    override fun update(binding: FragmentCallBinding) {
        super.update(binding)
        binding.remoteViewOverlay.visibility = View.VISIBLE
        binding.waitingView.startConnecting()
        binding.nextButton.isEnabled = false
    }
}

class Connected : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.CONNECTED
    override val messageId = R.string.connected
    
    override fun update(binding: FragmentCallBinding) {
        super.update(binding)
        binding.remoteViewOverlay.visibility = View.GONE
        binding.connectionStateView.hide()
        binding.waitingView.stop()
        binding.nextButton.isEnabled = true
    }
}

class Disconnected : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.DISCONNECTED
    override val messageId = R.string.disconnected
    
    override fun update(binding: FragmentCallBinding) {
        super.update(binding)
        binding.remoteViewOverlay.visibility = View.VISIBLE
        binding.nextButton.isEnabled = true
    }
}

class Failed : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.FAILED
    override val messageId = R.string.connection_failed
    
    override fun update(binding: FragmentCallBinding) {
        super.update(binding)
        binding.remoteViewOverlay.visibility = View.VISIBLE
        binding.nextButton.isEnabled = true
    }
}

class Closed : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.CLOSED
    override val messageId = R.string.connection_closed
    
    override fun update(binding: FragmentCallBinding) = Unit
}