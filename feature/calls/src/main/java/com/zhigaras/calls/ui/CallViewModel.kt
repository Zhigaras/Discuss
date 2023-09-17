package com.zhigaras.calls.ui

import com.google.firebase.auth.FirebaseAuth
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.calls.domain.CallCommunication
import com.zhigaras.calls.domain.CallsController
import com.zhigaras.webrtc.databinding.FragmentCallBinding

class CallViewModel(
    private val callsController: CallsController,
    communication: CallCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<FragmentCallBinding, CallUiState>(communication, dispatchers) {
    
    fun startCall(opponentId: String) {
        callsController.startNegotiation(opponentId)
    }
    
    fun init(binding: FragmentCallBinding) {
        callsController.initLocalView(binding.localView)
        callsController.initRemoteView(binding.remoteView)
    
        callsController.subscribeToConnectionEvents(FirebaseAuth.getInstance().uid.toString())
    }
    
    
}