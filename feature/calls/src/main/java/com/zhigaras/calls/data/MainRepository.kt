package com.zhigaras.calls.data

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.zhigaras.calls.domain.CallsCloudService
import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.calls.domain.model.ConnectionDataType
import com.zhigaras.calls.webrtc.NewEventCallBack
import com.zhigaras.calls.webrtc.SimplePeerConnectionObserver
import com.zhigaras.calls.webrtc.WebRtcClientImpl
import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.cloudeservice.CloudServiceImpl
import com.zhigaras.cloudeservice.ProvideDatabase
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection.PeerConnectionState
import org.webrtc.SurfaceViewRenderer

class MainRepository(
    application: Context,
    var listener: Listener? = null,
    private val callsCloudService: CallsCloudService =
        CallsCloudService.Base(CloudServiceImpl(ProvideDatabase.Base()))
) {
    private lateinit var webRtcClient: WebRtcClientImpl
    private val currentUsername: String = FirebaseAuth.getInstance().uid ?: "no id"
    private var remoteView: SurfaceViewRenderer? = null
    private val target: String = if (FirebaseAuth.getInstance().uid == "uzZAvzvRrFNoZz1p2xCrsdmpt4T2")
        "stmSRe5bcxNEmCTMm3bCth15vyr2"
        else "uzZAvzvRrFNoZz1p2xCrsdmpt4T2"
    
    init {
        webRtcClient = WebRtcClientImpl(application, object : SimplePeerConnectionObserver {
            override fun onAddStream(mediaStream: MediaStream) {
                super.onAddStream(mediaStream)
                try {
                    mediaStream.videoTracks[0].addSink(remoteView)
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
            
            override fun onIceCandidate(iceCandidate: IceCandidate) {
                super.onIceCandidate(iceCandidate)
                webRtcClient.sendIceCandidate(iceCandidate, target)
            }
        })
    }
    
    fun initLocalView(view: SurfaceViewRenderer) {
        webRtcClient.initLocalSurfaceView(view)
    }
    
    fun initRemoteView(view: SurfaceViewRenderer) {
        webRtcClient.initRemoteSurfaceView(view)
        remoteView = view
    }
    
    fun startCall(target: String) {
        webRtcClient.call(target)
    }
    
    fun switchCamera() {
        webRtcClient.switchCamera()
    }
    
    fun sendCallRequest(target: String) {
        webRtcClient.call(target)
//        callsCloudService.sendToCloud(
//            ConnectionData(target, currentUsername, type = ConnectionDataType.START_CALL),
//        )
    }
    
    fun endCall() {
        webRtcClient.closeConnection()
    }
    
    fun subscribeForLatestEvent() {
        callsCloudService.observeUpdates(
            currentUsername,
            object : CloudService.Callback<ConnectionData> {
                override fun provide(obj: ConnectionData) {
//                    target = obj.sender
                    obj.handle(webRtcClient)
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
