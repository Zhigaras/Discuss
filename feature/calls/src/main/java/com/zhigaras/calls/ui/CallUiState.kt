package com.zhigaras.calls.ui

import com.zhigaras.core.UiState
import com.zhigaras.webrtc.databinding.FragmentCallBinding
import org.webrtc.PeerConnection.PeerConnectionState

interface CallUiState : UiState<FragmentCallBinding> {
    
    object LookingForOpponent : CallUiState {
        
        override fun update(binding: FragmentCallBinding) {
            binding.testText.text = this::class.java.name
        }
    }
    
    object WaitingForOpponent : CallUiState {
        
        override fun update(binding: FragmentCallBinding) {
            binding.testText.text = this::class.java.name
        }
    }
    
    abstract class Connection : CallUiState {
        
        protected abstract val connectionState: PeerConnectionState
        
        fun match(state: PeerConnectionState) = connectionState == state
    }
}

object New : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.NEW
    override fun update(binding: FragmentCallBinding) {
        binding.testText.text = this::class.java.name
    }
}

object Connecting : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.CONNECTING
    override fun update(binding: FragmentCallBinding) {
        binding.testText.text = this::class.java.name
    }
}

object Connected : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.CONNECTED
    
    override fun update(binding: FragmentCallBinding) {
        binding.testText.text = this::class.java.name
    }
}

object Disconnected : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.DISCONNECTED
    
    override fun update(binding: FragmentCallBinding) {
        binding.testText.text = this::class.java.name
    }
}

object Failed : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.FAILED
    override fun update(binding: FragmentCallBinding) {
        binding.testText.text = this::class.java.name
    }
}

object Closed : CallUiState.Connection() {
    
    override val connectionState = PeerConnectionState.CLOSED
    override fun update(binding: FragmentCallBinding) {
        binding.testText.text = this::class.java.name
    }
}