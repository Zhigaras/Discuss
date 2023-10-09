package com.zhigaras.calls.webrtc

import org.webrtc.AddIceObserver
import org.webrtc.IceCandidate
import org.webrtc.PeerConnection
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend inline fun suspendCreateSessionDescription(
    crossinline call: (SdpObserver) -> Unit
): SessionDescription = suspendCoroutine {
    call(object : SdpObserver {
        override fun onCreateSuccess(sessionDescription: SessionDescription) {
            it.resume(sessionDescription)
        }
        
        override fun onSetSuccess() = Unit
        
        override fun onCreateFailure(error: String?) {
            it.resumeWithException(RuntimeException(error))
        }
        
        override fun onSetFailure(p0: String?) = Unit
    })
}

suspend inline fun suspendSdpObserver(
    crossinline call: (SdpObserver) -> Unit
) = suspendCoroutine {
    call(object : SdpObserver {
        override fun onCreateSuccess(p0: SessionDescription?) = Unit
        
        override fun onSetSuccess() {
            it.resume(Unit)
        }
        
        override fun onCreateFailure(p0: String?) = Unit
        
        override fun onSetFailure(error: String?) {
            it.resumeWithException(RuntimeException(error))
        }
    })
}

suspend fun PeerConnection.addRtcIceCandidate(iceCandidate: IceCandidate) {
    return suspendCoroutine {
        addIceCandidate(iceCandidate, object : AddIceObserver {
            override fun onAddSuccess() {
                it.resume(Unit)
            }
            
            override fun onAddFailure(error: String?) {
                it.resumeWithException(RuntimeException(error))
            }
        })
    }
}
