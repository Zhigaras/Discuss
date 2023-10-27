package com.zhigaras.calls.ui

import android.view.View
import com.zhigaras.core.UiState
import com.zhigaras.webrtc.databinding.FragmentCallBinding
import org.webrtc.PeerConnection.PeerConnectionState

interface CallUiState : UiState<FragmentCallBinding> {
    
    class LookingForOpponent : CallUiState {
        
        override fun update(binding: FragmentCallBinding) {
            binding.interruptedView.root.visibility = View.GONE
            binding.testText.text = this::class.java.name
            binding.waitingView.startSearch()
        }
    }
    
    class WaitingForOpponent : CallUiState {
        
        override fun update(binding: FragmentCallBinding) {
            binding.interruptedView.root.visibility = View.GONE
            binding.testText.text = this::class.java.name
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
        binding.interruptedView.root.visibility = View.GONE
        binding.testText.text = this::class.java.name
    }
}

class Connecting : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.CONNECTING
    override fun update(binding: FragmentCallBinding) {
        binding.interruptedView.root.visibility = View.GONE
        binding.testText.text = this::class.java.name
        binding.waitingView.startConnecting()
    }
}

class Connected : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.CONNECTED
    
    override fun update(binding: FragmentCallBinding) {
        binding.interruptedView.root.visibility = View.GONE
        binding.testText.text = this::class.java.name
        binding.waitingView.stop()
    }
}

class Disconnected : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.DISCONNECTED
    
    override fun update(binding: FragmentCallBinding) {
        binding.interruptedView.root.visibility = View.GONE
        binding.testText.text = this::class.java.name
    }
}

class Failed : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.FAILED
    override fun update(binding: FragmentCallBinding) {
        binding.interruptedView.root.visibility = View.GONE
        binding.testText.text = this::class.java.name
    }
}

class Closed : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.CLOSED
    override fun update(binding: FragmentCallBinding) {
        binding.interruptedView.root.visibility = View.GONE
        binding.testText.text = this::class.java.name
    }
}

class InterruptedByOpponent : CallUiState {
    
    override fun update(binding: FragmentCallBinding) {
        binding.interruptedView.root.visibility = View.VISIBLE
    }
}