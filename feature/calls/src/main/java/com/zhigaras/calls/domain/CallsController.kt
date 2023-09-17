package com.zhigaras.calls.domain

import org.webrtc.SurfaceViewRenderer

interface CallsController {
    
    fun startNegotiation(opponentId: String, userId: String)
    
    fun subscribeToConnectionEvents(userId: String)
    
    fun initLocalView(view: SurfaceViewRenderer)
    
    fun initRemoteView(view: SurfaceViewRenderer)
    
    fun setOpponentId(opponentId: String)
}