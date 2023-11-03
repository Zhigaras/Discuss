package com.zhigaras.home.presentation

import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.zhigaras.calls.domain.CallRoutes
import com.zhigaras.calls.domain.model.DisputeParty
import com.zhigaras.calls.domain.model.ReadyToCallUser
import com.zhigaras.cloudservice.CloudService
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.core.ProvideUserId
import com.zhigaras.home.domain.HomeCommunication
import com.zhigaras.home.domain.HomeInteractor
import com.zhigaras.home.domain.NavigateToCall
import com.zhigaras.home.domain.model.HomeTopic

class HomeViewModel(
    private val navigateToCall: NavigateToCall,
    private val provideUserId: ProvideUserId,
    private val homeInteractor: HomeInteractor,
    override val communication: HomeCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<HomeUiState>(dispatchers) {
    
    private val callback =
        object : CloudService.Callback<List<HomeTopic>> { // TODO: replace with coroutines??
            override fun provide(data: List<HomeTopic>) {
                communication.postBackground(HomeUiState.NewTopicList(data))
            }
            
            override fun error(message: String) {
                communication.postBackground(HomeUiState.Error(message))
            }
        }
    
    init {
        homeInteractor.subscribeToTopics(callback)
    }
    
    fun navigateToCall(topicId: Int, disputeParty: DisputeParty) {
        val user = ReadyToCallUser(provideUserId.provide(), topicId, disputeParty)
        navigateToCall.navigateToCall(bundleOf(CallRoutes.READY_TO_CALL_USER_KEY to user))
    }
    
    override fun observe(owner: LifecycleOwner, observer: Observer<HomeUiState>) {
        super.observe(owner, observer)
//        homeInteractor.observeNetwork(owner, communication)
    }
    
    override fun onCleared() {
        homeInteractor.removeCallback(callback)
        super.onCleared()
    }
}