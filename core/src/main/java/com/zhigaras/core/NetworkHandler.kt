package com.zhigaras.core

import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

interface NetworkHandler : NetworkCommunication.Observe, NetworkCommunication.ObserveForever {
    
    class Base(
        connManager: ConnectivityManager,
        private val communication: NetworkCommunication.Mutable
    ) : NetworkHandler {
        
        private val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                communication.postBackground(NetworkState.Available())
            }
            
            override fun onLosing(network: Network, maxMsToLive: Int) {
                communication.postBackground(NetworkState.Loosing())
            }
            
            override fun onLost(network: Network) {
                communication.postBackground(NetworkState.Lost())
            }
            
            override fun onUnavailable() {
                communication.postBackground(NetworkState.Unavailable())
            }
        }
        
        init {
            connManager.registerDefaultNetworkCallback(callback)
        }
        
        override fun observe(owner: LifecycleOwner, observer: Observer<NetworkState>) {
            communication.observe(owner, observer)
        }
        
        override fun observeForever(observer: Observer<NetworkState>) {
            communication.observeForever(observer)
        }
        
        override fun removeObserver(observer: Observer<NetworkState>) {
            communication.removeObserver(observer)
        }
    }
}