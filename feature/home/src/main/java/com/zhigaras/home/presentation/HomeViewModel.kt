package com.zhigaras.home.presentation

import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import com.zhigaras.calls.domain.CallRoutes
import com.zhigaras.calls.domain.model.DisputeParty
import com.zhigaras.calls.domain.model.ReadyToCallUser
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.core.ProvideUserId
import com.zhigaras.home.domain.HomeCommunication
import com.zhigaras.home.domain.HomeInteractor
import com.zhigaras.home.domain.NavigateToCall
import com.zhigaras.home.domain.NavigateToProfile
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val navigateToCall: NavigateToCall,
    private val navigateToProfile: NavigateToProfile,
    private val provideUserId: ProvideUserId,
    private val homeInteractor: HomeInteractor,
    override val uiCommunication: HomeCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<HomeUiState>(dispatchers), NavigateToProfile {
    private val topicsFlowJob = viewModelScope.launch {
        homeInteractor.subscribeToTopics().catch {
            uiCommunication.postBackground(HomeUiState.DataError(it.message!!)) // TODO: escape of !!
        }.collect {
            uiCommunication.postBackground(HomeUiState.NewTopicList(it))
        }
    }
    
    fun navigateToCall(topicId: Int, disputeParty: DisputeParty) {
        if (homeInteractor.isOnline()) {
            val user = ReadyToCallUser(provideUserId.provide(), topicId, disputeParty)
            navigateToCall.navigateToCall(bundleOf(CallRoutes.READY_TO_CALL_USER_KEY to user))
        } else {
            uiCommunication.postBackground(HomeUiState.CantGoToCall())
        }
        
    }
    
    override fun navigateToProfile() {
        navigateToProfile.navigateToProfile()
    }
    
    override fun onCleared() {
        topicsFlowJob.cancel()
        super.onCleared()
    }
}