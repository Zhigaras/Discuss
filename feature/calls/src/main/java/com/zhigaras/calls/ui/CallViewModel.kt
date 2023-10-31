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
import org.koin.core.component.KoinScopeComponent
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.webrtc.SurfaceViewRenderer

class CallViewModel(
    private val initCalls: InitCalls,
    private val callsController: CallsController,
    private val matchingInteractor: MatchingInteractor,
    private val routes: CallRoutes,
    override val communication: CallCommunication.Mutable,
    provideUserId: ProvideUserId,
    dispatchers: Dispatchers
) : BaseViewModel<CallUiState>(dispatchers), KoinScopeComponent {
    
    override val scope: Scope = getKoin().createScope(SCOPE_ID, named("callViewModel"))
    
    
    init {
        initCalls.subscribeToConnectionEvents(provideUserId.provide())
    }
    
    fun init(localView: SurfaceViewRenderer, remoteView: SurfaceViewRenderer) {
        initCalls.initLocalView(localView)
        initCalls.initRemoteView(remoteView)
    }
    
    fun lookForOpponent(user: ReadyToCallUser) {
        viewModelScope.launch {
            initCalls.initUser(user)
            communication.postUi(CallUiState.LookingForOpponent())
            matchingInteractor.checkMatching(user)
                .handle(callsController, matchingInteractor, communication)
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
    
    companion object {
        const val SCOPE_ID = "callViewModelScope"
    }
}