package com.zhigaras.calls.ui

import android.view.View
import androidx.annotation.CallSuper
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
        }
    }
    
    class WaitingForOpponent : CallUiState {
        
        override fun update(binding: FragmentCallBinding) {
            binding.remoteViewOverlay.visibility = View.VISIBLE
            binding.connectionStateView.hide()
            binding.waitingView.startWaiting()
        }
    }
    
    class InterruptedByOpponent : CallUiState {
        
        override fun update(binding: FragmentCallBinding) {
            binding.remoteViewOverlay.visibility = View.VISIBLE
            binding.connectionStateView.show(R.string.interrupted_by_opponent)
        }
    }
    
    class CheckConnection : CallUiState {
        
        override fun update(binding: FragmentCallBinding) {
            binding.remoteViewOverlay.visibility = View.VISIBLE
            binding.connectionStateView.show(R.string.check_connection)
        }
    }
    
    class TryingToReconnect : CallUiState {
        
        override fun update(binding: FragmentCallBinding) {
            binding.remoteViewOverlay.visibility = View.VISIBLE
            binding.waitingView.startReconnect()
        }
    }
    
    class Error(private val message: String) : CallUiState {
        
        override fun update(binding: FragmentCallBinding) {
            binding.remoteViewOverlay.visibility = View.VISIBLE
            binding.connectionStateView.show(R.string.connection_error, message)
        }
    }
    
    abstract class Connection : CallUiState {
        
        protected abstract val connectionState: PeerConnectionState
        protected abstract val messageId: Int
        
        fun match(state: PeerConnectionState) = connectionState == state
        
        @CallSuper
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
    }
}

class Disconnected : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.DISCONNECTED
    override val messageId = R.string.disconnected
    
    override fun update(binding: FragmentCallBinding) {
        super.update(binding)
        binding.remoteViewOverlay.visibility = View.VISIBLE
    }
}

class Failed : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.FAILED
    override val messageId = R.string.connection_failed
    
    override fun update(binding: FragmentCallBinding) {
        super.update(binding)
        binding.remoteViewOverlay.visibility = View.VISIBLE
    }
}

class Closed : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.CLOSED
    override val messageId = R.string.connection_closed
    
    override fun update(binding: FragmentCallBinding) {
        super.update(binding)
        binding.remoteViewOverlay.visibility = View.VISIBLE
    }
}