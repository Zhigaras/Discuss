package com.zhigaras.discuss.domain

import androidx.lifecycle.LifecycleOwner
import com.zhigaras.core.NetworkHandler
import com.zhigaras.core.NetworkState
import com.zhigaras.discuss.presentation.MainNetworkUiState

interface MainInteractor {
    
    fun observeNetwork(owner: LifecycleOwner, communication: MainUiStateCommunication.Post)
    
    class Base(private val networkHandler: NetworkHandler) : MainInteractor {
        
        inner class MainNetworkStateFactory {
            
            private val states = listOf(
                MainNetworkUiState.Available(),
                MainNetworkUiState.Unavailable(),
                MainNetworkUiState.Loosing(),
                MainNetworkUiState.Lost()
            )
            
            fun state(networkState: NetworkState) = states.find { it.matches(networkState) }!!
        }
        
        override fun observeNetwork(
            owner: LifecycleOwner,
            communication: MainUiStateCommunication.Post
        ) {
            networkHandler.observe(owner) {
                val state = MainNetworkStateFactory().state(it)
                communication.postUi(state)
            }
        }
    }
}