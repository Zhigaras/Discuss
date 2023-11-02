package com.zhigaras.calls.domain

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.zhigaras.calls.webrtc.PeerConnectionCallback
import com.zhigaras.calls.webrtc.WebRtcClient
import com.zhigaras.core.IntentAction
import com.zhigaras.core.InternetConnectionState
import org.webrtc.PeerConnection

class ConnectionStateReceiver(
    private val peerConnectionCallback: PeerConnectionCallback,
    private val webRtcClient: WebRtcClient,
    private val restart: () -> Unit
) : BroadcastReceiver() {
    
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == IntentAction.ACTION_NETWORK_STATE) {
            val networkState = InternetConnectionState.valueOf(
                intent.getStringExtra("state")
                    ?: InternetConnectionState.UNKNOWN.name
            )
            val connState = webRtcClient.provideConnectionState()
            if (networkState == InternetConnectionState.ONLINE) {
                if (connState == PeerConnection.PeerConnectionState.DISCONNECTED ||
                    connState == PeerConnection.PeerConnectionState.FAILED
                ) {
                    peerConnectionCallback.postTryingToReconnect()
                    restart.invoke()
                }
            } else {
                peerConnectionCallback.postCheckConnection()
            }
        }
    }
}