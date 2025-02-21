package com.zhigaras.discuss.domain

import com.zhigaras.core.NetworkHandler
import com.zhigaras.core.NetworkState
import com.zhigaras.discuss.presentation.MainNetworkUiState

interface MainInteractor {

    suspend fun observeNetwork(flux: MainUiStateFlux.Post): Nothing

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

        override suspend fun observeNetwork(flux: MainUiStateFlux.Post): Nothing = networkHandler.collect {
            val state = MainNetworkStateFactory().state(it)
            flux.post(state)
        }
    }
}
