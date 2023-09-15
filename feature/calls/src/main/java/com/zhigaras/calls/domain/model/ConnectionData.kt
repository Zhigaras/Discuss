package com.zhigaras.calls.domain.model

import com.google.gson.Gson
import com.zhigaras.calls.webrtc.WebRtcClientImpl
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription

class ConnectionData(
    val target: String = "",
    val sender: String = "",
    val data: String = "",
    val type: ConnectionDataType = ConnectionDataType.EMPTY
) : HandleConnectionData {
    override fun handle(client: WebRtcClientImpl) {
        type.handle(client, this)
    }
}

interface HandleConnectionData {
    
    fun handle(client: WebRtcClientImpl)
}

enum class ConnectionDataType {
    
    EMPTY {
        override fun handle(client: WebRtcClientImpl, connectionData: ConnectionData) {
            // TODO: handle start state (before connection)
        }
    },
    OFFER {
        override fun handle(client: WebRtcClientImpl, connectionData: ConnectionData) {
            client.onRemoteSessionReceived(
                SessionDescription(
                    SessionDescription.Type.OFFER, connectionData.data
                )
            )
            client.answer(connectionData.sender)
        }
    },
    ANSWER {
        override fun handle(client: WebRtcClientImpl, connectionData: ConnectionData) {
            client.onRemoteSessionReceived(
                SessionDescription(
                    SessionDescription.Type.ANSWER, connectionData.data
                )
            )
        }
    },
    ICE_CANDIDATE {
        override fun handle(client: WebRtcClientImpl, connectionData: ConnectionData) {
            try {
                val candidate: IceCandidate =
                    gson.fromJson(connectionData.data, IceCandidate::class.java)
                client.addIceCandidate(candidate)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    },
    START_CALL {
        override fun handle(client: WebRtcClientImpl, connectionData: ConnectionData) {
        
        }
        
    };
    
    val gson: Gson = Gson() // TODO: how to escape of it??
    
    abstract fun handle(client: WebRtcClientImpl, connectionData: ConnectionData)
}
