package com.zhigaras.calls.ui

import com.zhigaras.calls.domain.CallCommunication
import com.zhigaras.calls.domain.CallRoutes
import com.zhigaras.calls.domain.CallsController
import com.zhigaras.calls.domain.InitCalls
import com.zhigaras.calls.domain.MatchingInteractor
import com.zhigaras.calls.domain.model.ReadyToCallUser
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.core.ProvideUserId
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
    
    fun handleNextOpponentClick(user: ReadyToCallUser?, showDialog: () -> Unit) {
        if (callsController.isConnected()) showDialog.invoke()
        else user?.let { nextOpponent(it) }
        
    }
    
    fun handleEndConversationClick(showDialog: () -> Unit) {
        if (callsController.isConnected()) showDialog.invoke()
        else endConversation()
    }
    
    fun lookForOpponent(user: ReadyToCallUser) {
        initCalls.initUser(user)
        uiCommunication.postUi(CallUiState.LookingForOpponent())
        scopeLaunch(
            onBackground = {matchingInteractor.checkMatching(user)},
            onUi = {it.handle(callsController, matchingInteractor, uiCommunication)}
        )
    }
    
    fun nextOpponent(user: ReadyToCallUser) {
        callsController.sendInterruptionToOpponent()
        callsController.closeCurrentConnection()
        callsController.createNewConnection()
        lookForOpponent(user)
    }
    
    fun endConversation() {
        callsController.sendInterruptionToOpponent()
        callsController.closeConnectionTotally()
        routes.goBack()
    }
}