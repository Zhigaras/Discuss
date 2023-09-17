package com.zhigaras.calls.ui

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.calls.domain.CallCommunication
import com.zhigaras.calls.domain.CallsController
import com.zhigaras.calls.domain.CallsInteractor
import com.zhigaras.calls.domain.model.DisputePosition
import com.zhigaras.webrtc.databinding.FragmentCallBinding
import kotlinx.coroutines.launch

class CallViewModel(
    private val callsController: CallsController,
    private val callsInteractor: CallsInteractor,
    communication: CallCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<FragmentCallBinding, CallUiState>(communication, dispatchers) {
    
    fun startCall(opponentId: String) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().uid!!
            val opinion = if (userId == "stmSRe5bcxNEmCTMm3bCth15vyr2") DisputePosition.SUPPORT else DisputePosition.AGAINST
            callsInteractor.checkMatching("1", userId, opinion).let {
                it.handle(callsController, callsInteractor)
            }
        }
    }
    
    fun init(binding: FragmentCallBinding) {
        callsController.initLocalView(binding.localView)
        callsController.initRemoteView(binding.remoteView)
    
        callsController.subscribeToConnectionEvents(FirebaseAuth.getInstance().uid.toString())
    }
    
    
}