package com.zhigaras.home.presentation

import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import com.zhigaras.calls.domain.CallRoutes
import com.zhigaras.calls.domain.model.DisputeParty
import com.zhigaras.calls.domain.model.ReadyToCallUser
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.core.ProvideUserId
import com.zhigaras.home.domain.HomeUiStateFlux
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
    private val uiStateFlux: HomeUiStateFlux.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<HomeUiState>(dispatchers, uiStateFlux) {
    private val topicsFlowJob = viewModelScope.launch {
        homeInteractor.subscribeToTopics().catch {
            uiStateFlux.post(HomeUiState.DataError(it.message!!)) // TODO: escape of !!
        }.collect {
            uiStateFlux.post(HomeUiState.NewTopicList(it))
        }
    }

    fun navigateToCall(topicId: Int, disputeParty: DisputeParty) = safeLaunch {
        if (homeInteractor.isOnline()) {
            val user = ReadyToCallUser(provideUserId.provide(), topicId, disputeParty)
            navigateToCall.navigateToCall(bundleOf(CallRoutes.READY_TO_CALL_USER_KEY to user))
        } else {
            uiStateFlux.post(HomeUiState.CantGoToCall())
        }
    }

    fun navigateToProfile() = safeLaunch {
        navigateToProfile.navigateToProfile()
    }

    override fun onCleared() {
        topicsFlowJob.cancel()
        super.onCleared()
    }
}
