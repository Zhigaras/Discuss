package com.zhigaras.calls.ui

import androidx.lifecycle.viewModelScope
import com.zhigaras.auth.ProvideUserId
import com.zhigaras.calls.domain.CallCommunication
import com.zhigaras.calls.domain.CallsController
import com.zhigaras.calls.domain.MatchingInteractor
import com.zhigaras.calls.domain.model.DisputePosition
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.webrtc.databinding.FragmentCallBinding
import kotlinx.coroutines.launch
import org.webrtc.SurfaceViewRenderer

class CallViewModel(
    private val callsController: CallsController,
    private val matchingInteractor: MatchingInteractor,
    private val provideUserId: ProvideUserId,
    communication: CallCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<FragmentCallBinding, CallUiState>(communication, dispatchers) {
    
    fun lookForOpponent(subjectId: String, opinion: DisputePosition) {
        viewModelScope.launch {
            matchingInteractor.checkMatching(subjectId, provideUserId.provide(), opinion).let {
                it.handle(callsController, matchingInteractor)
            }
        }
    }
    
    fun init(localView: SurfaceViewRenderer, remoteView: SurfaceViewRenderer) {
        callsController.initLocalView(localView)
        callsController.initRemoteView(remoteView)
    }
}