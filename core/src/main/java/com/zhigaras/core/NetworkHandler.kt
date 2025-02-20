package com.zhigaras.core

import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.flow.FlowCollector

interface NetworkHandler : NetworkStateFlux.Observe {
    class Base(
        connManager: ConnectivityManager,
        private val communication: NetworkStateFlux.Mutable
    ) : NetworkHandler {

        private val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                communication.post(NetworkState.Available())
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                communication.post(NetworkState.Loosing())
            }

            override fun onLost(network: Network) {
                communication.post(NetworkState.Lost())
            }

            override fun onUnavailable() {
                communication.post(NetworkState.Unavailable())
            }
        }

        init {
            connManager.registerDefaultNetworkCallback(callback)
        }

        override suspend fun collect(collector: FlowCollector<NetworkState>) = communication.collect(collector)
        override fun current(): NetworkState = communication.current()
    }
}
