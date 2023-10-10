package com.zhigaras.calls.domain.model

import org.webrtc.SessionDescription

class MySessionDescription(sessionDescription: SessionDescription? = null) :
    SessionDescription(sessionDescription?.type, sessionDescription?.description ?: "") {
    override fun toString(): String {
        return "type = $type, desc = $description"
    }
}