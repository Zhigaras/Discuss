package com.zhigaras.calls.ui

import androidx.lifecycle.viewModelScope
import com.zhigaras.calls.domain.CallCommunication
import com.zhigaras.calls.domain.CallRoutes
import com.zhigaras.calls.domain.CallsController
import com.zhigaras.calls.domain.InitCalls
import com.zhigaras.calls.domain.MatchingInteractor
import com.zhigaras.calls.domain.model.ReadyToCallUser
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.core.ProvideUserId
import kotlinx.coroutines.launch
import org.webrtc.SurfaceViewRenderer

class CallViewModel(
    private val initCalls: InitCalls,
    private val callsController: CallsController,
    private val matchingInteractor: MatchingInteractor,
    private val routes: CallRoutes,
    override val uiCommunication: CallCommunication.Mutable,
    provideUserId: ProvideUserId,
    dispatchers: Dispatchers
) : BaseViewModel<CallUiState>(dispatchers) {
    
    init {
        initCalls.subscribeToConnectionEvents(provideUserId.provide())
    }
    
    fun init(localView: SurfaceViewRenderer, remoteView: SurfaceViewRenderer) {
        initCalls.initLocalView(localView)
        initCalls.initRemoteView(remoteView)
    }
    
    fun lookForOpponent(user: ReadyToCallUser) { // TODO: replace with scopeLaunch()
        viewModelScope.launch {
            initCalls.initUser(user)
            uiCommunication.postUi(CallUiState.LookingForOpponent())
            matchingInteractor.checkMatching(user)
                .handle(callsController, matchingInteractor, uiCommunication)
        }
    }
    
    fun nextOpponent(user: ReadyToCallUser) {
        callsController.sendInterruptionToOpponent()
        callsController.closeCurrentConnection()
        callsController.createNewConnection()
        lookForOpponent(user)
    }
    
    fun closeConnection() {
        callsController.sendInterruptionToOpponent()
        callsController.closeConnectionTotally()
        routes.goBack()
    }
}