package com.zhigaras.calls.domain.model

import android.util.Log
import com.google.gson.Gson
import com.zhigaras.calls.webrtc.WebRtcClient
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription

data class ConnectionData(
    val target: String = "",
    val sender: String = "",
    val data: String = "",
    val type: ConnectionDataType = ConnectionDataType.EMPTY
) : HandleConnectionData {
    override fun handle(client: WebRtcClient) {
        type.handle(client, this)
    }
}

interface HandleConnectionData {
    
    fun handle(client: WebRtcClient)
}

enum class ConnectionDataType {
    
    EMPTY {
        override fun handle(client: WebRtcClient, connectionData: ConnectionData) {
            Log.d("AAA trouble empty data", "nothing to do")
        }
    },
    OFFER {
        override fun handle(client: WebRtcClient, connectionData: ConnectionData) {
            Log.d("AAA trouble OFFER handle", "offer handled")
            client.onRemoteSessionReceived(
                SessionDescription(
                    SessionDescription.Type.OFFER, connectionData.data
                )
            )
            client.answer(connectionData.sender, connectionData.target)
        }
    },
    ANSWER {
        override fun handle(client: WebRtcClient, connectionData: ConnectionData) {
            Log.d("AAA trouble ANSWER handle", "answer handled")
            client.onRemoteSessionReceived(
                SessionDescription(
                    SessionDescription.Type.ANSWER, connectionData.data
                )
            )
        }
    },
    ICE_CANDIDATE {
        override fun handle(client: WebRtcClient, connectionData: ConnectionData) {
            Log.d("AAA trouble CANDIDATE handle", "iceCandidate handled")
            try {
                val candidate: IceCandidate =
                    gson.fromJson(connectionData.data, IceCandidate::class.java)
                client.addIceCandidate(candidate)
            } catch (e: Exception) {
                Log.d("AAA trouble CANDIDATE error", e.message.toString()) // TODO: fix this
            }
        }
    };
    
    val gson: Gson = Gson() // TODO: how to escape of it??
    
    abstract fun handle(client: WebRtcClient, connectionData: ConnectionData)
}
