package com.zhigaras.calls.domain

import com.zhigaras.calls.domain.model.ConnectionData
import org.webrtc.SurfaceViewRenderer

interface CallsController {
    
    fun startNegotiation(opponentId: String)
    
    fun subscribeToConnectionEvents(userId: String)
    
    fun initLocalView(view: SurfaceViewRenderer)
    
    fun initRemoteView(view: SurfaceViewRenderer)
    
    fun setOpponentId(opponentId: String)
}