package com.zhigaras.calls.ui

import androidx.lifecycle.viewModelScope
import com.zhigaras.auth.ProvideUserId
import com.zhigaras.calls.domain.CallCommunication
import com.zhigaras.calls.domain.CallRoutes
import com.zhigaras.calls.domain.CallsController
import com.zhigaras.calls.domain.InitCalls
import com.zhigaras.calls.domain.MatchingInteractor
import com.zhigaras.calls.domain.model.DisputeParty
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.webrtc.databinding.FragmentCallBinding
import kotlinx.coroutines.launch
import org.webrtc.SurfaceViewRenderer

class CallViewModel(
    private val initCalls: InitCalls,
    private val callsController: CallsController,
    private val matchingInteractor: MatchingInteractor,
    private val provideUserId: ProvideUserId,
    private val routes: CallRoutes,
    override val communication: CallCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<FragmentCallBinding, CallUiState>(dispatchers) {
    
    fun init(localView: SurfaceViewRenderer, remoteView: SurfaceViewRenderer) {
        initCalls.initLocalView(localView)
        initCalls.initRemoteView(remoteView)
    }
    
    fun lookForOpponent(subjectId: String, opinion: DisputeParty, isConnectionRecreated: Boolean = false) {
        viewModelScope.launch {
            communication.postUi(CallUiState.LookingForOpponent)
            matchingInteractor.checkMatching(subjectId, provideUserId.provide(), opinion,isConnectionRecreated)
                .handle(callsController, matchingInteractor, communication)
        }
    }
    
    fun nextOpponent(subjectId: String, opinion: DisputeParty) {
        callsController.closeCurrentAndCreateNewConnection()
        lookForOpponent(subjectId, opinion, true)
    }
    
    fun closeConnection() {
        callsController.closeConnectionTotally()
        routes.goBack()
    }
}