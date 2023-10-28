package com.zhigaras.calls.ui

import androidx.lifecycle.viewModelScope
import com.zhigaras.auth.ProvideUserId
import com.zhigaras.calls.domain.CallCommunication
import com.zhigaras.calls.domain.CallRoutes
import com.zhigaras.calls.domain.CallsController
import com.zhigaras.calls.domain.InitCalls
import com.zhigaras.calls.domain.MatchingInteractor
import com.zhigaras.calls.domain.model.DisputeParty
import com.zhigaras.calls.domain.model.ReadyToCallUser
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
    
    init {
        initCalls.subscribeToConnectionEvents(provideUserId.provide())
    }
    
    fun init(localView: SurfaceViewRenderer, remoteView: SurfaceViewRenderer) {
        initCalls.initLocalView(localView)
        initCalls.initRemoteView(remoteView)
    }
    
    fun lookForOpponent(subjectId: String, opinion: DisputeParty) {
        viewModelScope.launch {
            initCalls.initUser(ReadyToCallUser(provideUserId.provide(), subjectId, opinion))
            communication.postUi(CallUiState.LookingForOpponent())
            matchingInteractor
                .checkMatching(subjectId, provideUserId.provide(), opinion)
                .handle(callsController, matchingInteractor, communication)
        }
    }
    
    fun nextOpponent(subjectId: String, opinion: DisputeParty) {
        callsController.sendInterruptionToOpponent()
        callsController.closeCurrentConnection()
        callsController.createNewConnection()
        lookForOpponent(subjectId, opinion)
    }
    
    fun closeConnection() {
        callsController.sendInterruptionToOpponent()
        callsController.closeConnectionTotally()
        routes.goBack()
    }
}