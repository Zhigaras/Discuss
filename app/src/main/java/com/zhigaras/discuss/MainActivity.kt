package com.zhigaras.discuss

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.zhigaras.core.IntentAction
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    
    private val viewModel by viewModel<MainViewModel>()
    private lateinit var connManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(IntentAction.ACTION_NETWORK_STATE)
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                sendBroadcast(intent.putExtra("state", "online"))
                Log.d("QQQQQ activity", "available")
            }

            override fun onLost(network: Network) {
                sendBroadcast(intent.putExtra("state", "offline"))
                Log.d("QQQQQ activity", "lost")
            }
        }
        
        viewModel.observe(this) {
            it.show(supportFragmentManager, R.id.container)
        }
        viewModel.init(savedInstanceState == null)
        
        connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connManager.registerDefaultNetworkCallback(networkCallback)
    }
    
    override fun onPause() {
        super.onPause()
        connManager.unregisterNetworkCallback(networkCallback)
    }
}