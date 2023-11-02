package com.zhigaras.calls.domain

import android.net.ConnectivityManager
import android.net.Network

class NetworkStateCallback(
    private val onLost: () -> Unit,
    private val onAvailable: () -> Unit
) : ConnectivityManager.NetworkCallback() {
    
    override fun onAvailable(network: Network) {
        onAvailable.invoke()
    }
    
    override fun onLost(network: Network) {
        onLost.invoke()
    }
}