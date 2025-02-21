package com.zhigaras.core

import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.flow.FlowCollector

interface NetworkHandler : NetworkStateFlux.Observe {
    class Base(
        connManager: ConnectivityManager,
        private val flux: NetworkStateFlux.Mutable
    ) : NetworkHandler {

        private val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                flux.post(NetworkState.Available())
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                flux.post(NetworkState.Loosing())
            }

            override fun onLost(network: Network) {
                flux.post(NetworkState.Lost())
            }

            override fun onUnavailable() {
                flux.post(NetworkState.Unavailable())
            }
        }

        init {
            connManager.registerDefaultNetworkCallback(callback)
        }

        override suspend fun collect(collector: FlowCollector<NetworkState>) = flux.collect(collector)
        override fun current(): NetworkState = flux.current()
    }
}
