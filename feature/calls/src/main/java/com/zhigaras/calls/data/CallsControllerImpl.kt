package com.zhigaras.calls.data

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.zhigaras.calls.domain.CallsCloudService
import com.zhigaras.calls.domain.CallsController
import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.calls.webrtc.SimplePeerConnectionObserver
import com.zhigaras.calls.webrtc.WebRtcClientImpl
import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.cloudeservice.CloudServiceImpl
import com.zhigaras.cloudeservice.ProvideDatabase
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection.PeerConnectionState
import org.webrtc.SurfaceViewRenderer

class CallsControllerImpl(
    application: Context,
    private val callsCloudService: CallsCloudService =
        CallsCloudService.Base(CloudServiceImpl(ProvideDatabase.Base()))
): CallsController {
    private lateinit var webRtcClient: WebRtcClientImpl
    private val currentUsername: String = FirebaseAuth.getInstance().uid ?: "no id"
    private var remoteView: SurfaceViewRenderer? = null
    private lateinit var target: String
    
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
                
            }
            
            override fun onIceCandidate(iceCandidate: IceCandidate) {
                super.onIceCandidate(iceCandidate)
                webRtcClient.sendIceCandidate(iceCandidate, target)
            }
        })
    }
    
    override fun initLocalView(view: SurfaceViewRenderer) {
        webRtcClient.initLocalSurfaceView(view)
    }
    
    override fun initRemoteView(view: SurfaceViewRenderer) {
        webRtcClient.initRemoteSurfaceView(view)
        remoteView = view
    }
    
    fun switchCamera() {
        webRtcClient.switchCamera()
    }
    
    override fun startNegotiation(opponentId: String) {
        webRtcClient.call(opponentId)
    }
    
    override fun setOpponentId(opponentId: String) {
        target = opponentId
    }
    
    fun endCall() {
        webRtcClient.closeConnection()
    }
    
    override fun subscribeToConnectionEvents(userId: String){
        callsCloudService.observeUpdates(
            currentUsername,
            object : CloudService.Callback<ConnectionData> {
                override fun provide(obj: ConnectionData) {
                    target = obj.sender
                    obj.handle(webRtcClient)
                }
                
                override fun error(message: String) {
                    // TODO: handle errors
                }
            })
    }
}
