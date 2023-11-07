package com.zhigaras.discuss.presentation

import com.zhigaras.core.NetworkState
import com.zhigaras.core.UiState
import com.zhigaras.discuss.databinding.ActivityMainBinding

interface MainUiState : UiState<ActivityMainBinding>

interface MainNetworkUiState : MainUiState {
    
    fun matches(networkState: NetworkState): Boolean
    
    class Available : MainNetworkUiState {
        override fun matches(networkState: NetworkState) = networkState is NetworkState.Available
        
        override fun update(binding: ActivityMainBinding) {
            binding.connectionStateView.hideConnectionLost()
        }
    }
    
    class Unavailable : MainNetworkUiState {
        override fun matches(networkState: NetworkState) = networkState is NetworkState.Unavailable
        
        override fun update(binding: ActivityMainBinding) {
            binding.connectionStateView.showConnectionLost()
        }
    }
    
    class Loosing : MainNetworkUiState {
        override fun matches(networkState: NetworkState) = networkState is NetworkState.Loosing
        
        override fun update(binding: ActivityMainBinding) {
        
        }
    }
    
    class Lost : MainNetworkUiState {
        override fun matches(networkState: NetworkState) = networkState is NetworkState.Lost
        
        override fun update(binding: ActivityMainBinding) {
            binding.connectionStateView.showConnectionLost()
        }
    }
}