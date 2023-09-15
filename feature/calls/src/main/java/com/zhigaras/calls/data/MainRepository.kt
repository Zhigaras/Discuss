package com.zhigaras.calls.data

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.zhigaras.calls.domain.CallsCloudService
import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.calls.domain.model.ConnectionDataType
import com.zhigaras.calls.webrtc.NewEventCallBack
import com.zhigaras.calls.webrtc.SimplePeerConnectionObserver
import com.zhigaras.calls.webrtc.SuccessCallBack
import com.zhigaras.calls.webrtc.WebRTCClient
import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.cloudeservice.CloudServiceImpl
import com.zhigaras.cloudeservice.ProvideDatabase
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection.PeerConnectionState
import org.webrtc.SessionDescription
import org.webrtc.SurfaceViewRenderer

object MainRepository {
    var listener: Listener? = null
    private val gson = Gson()
    private val callsCloudService: CallsCloudService =
        CallsCloudService.Base(CloudServiceImpl(ProvideDatabase.Base()))
    private var webRTCClient: WebRTCClient? = null
    private val currentUsername: String = FirebaseAuth.getInstance().uid ?: "no id"
    private var remoteView: SurfaceViewRenderer? = null
    private lateinit var target: String
//    private fun updateCurrentUsername(username: String) {
//        currentUsername = username
//    }
    
    fun login(username: String, context: Context, callBack: SuccessCallBack) {
//        firebaseClient.login(username, object : SuccessCallBack {
//            override fun onSuccess() {
//                updateCurrentUsername(username)
        webRTCClient = WebRTCClient(context, object : SimplePeerConnectionObserver() {
            override fun onAddStream(mediaStream: MediaStream?) {
                super.onAddStream(mediaStream)
                try {
                    mediaStream!!.videoTracks[0].addSink(remoteView)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            
            override fun onConnectionChange(newState: PeerConnectionState) {
                Log.d("TAG", "onConnectionChange: $newState")
                super.onConnectionChange(newState)
                if (newState == PeerConnectionState.CONNECTED && listener != null) {
                    listener!!.webrtcConnected()
                }
                if (newState == PeerConnectionState.CLOSED ||
                    newState == PeerConnectionState.DISCONNECTED
                ) {
                    if (listener != null) {
                        listener!!.webrtcClosed()
                    }
                }
            }
            
            override fun onIceCandidate(iceCandidate: IceCandidate?) {
                super.onIceCandidate(iceCandidate)
                webRTCClient?.sendIceCandidate(iceCandidate, target)
            }
        })
        callBack.onSuccess()
    }
//        })
//    }
    
    fun initLocalView(view: SurfaceViewRenderer) {
        webRTCClient?.initLocalSurfaceView(view)
    }
    
    fun initRemoteView(view: SurfaceViewRenderer) {
        webRTCClient?.initRemoteSurfaceView(view)
        remoteView = view
    }
    
    fun startCall(target: String) {
        webRTCClient?.call(target)
    }
    
    fun switchCamera() {
        webRTCClient?.switchCamera()
    }
    
    fun toggleAudio(shouldBeMuted: Boolean?) {
        webRTCClient?.toggleAudio(shouldBeMuted)
    }
    
    fun toggleVideo(shouldBeMuted: Boolean?) {
        webRTCClient?.toggleVideo(shouldBeMuted)
    }
    
    fun sendCallRequest(target: String) {
        callsCloudService.sendToCloud(
            ConnectionData(target, currentUsername, type = ConnectionDataType.START_CALL),
        )
    }
    
    fun endCall() {
        webRTCClient?.closeConnection()
    }
    
    fun subscribeForLatestEvent(callBack: NewEventCallBack) {
        callsCloudService.observeUpdates(
            currentUsername,
            object : CloudService.Callback<ConnectionData> {
                override fun provide(obj: ConnectionData) {
                    when (obj.type) {
                        ConnectionDataType.OFFER -> {
                            target = obj.sender
                            webRTCClient?.onRemoteSessionReceived(
                                SessionDescription(
                                    SessionDescription.Type.OFFER, obj.data
                                )
                            )
                            webRTCClient?.answer(obj.sender)
                        }
                        
                        ConnectionDataType.ANSWER -> {
                            target = obj.sender
                            webRTCClient?.onRemoteSessionReceived(
                                SessionDescription(
                                    SessionDescription.Type.ANSWER, obj.data
                                )
                            )
                        }
                        
                        ConnectionDataType.ICE_CANDIDATE -> try {
                            val candidate: IceCandidate =
                                gson.fromJson(obj.data, IceCandidate::class.java)
                            webRTCClient?.addIceCandidate(candidate)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        
                        ConnectionDataType.START_CALL -> {
                            target = obj.sender
                            callBack.onNewEventReceived(obj)
                        }
                        
                        ConnectionDataType.EMPTY -> {}
                    }
                }
                
                override fun error(message: String) {
                    // TODO: handle errors
                }
                
            })
    }
    
    interface Listener {
        fun webrtcConnected()
        fun webrtcClosed()
    }
    
}
