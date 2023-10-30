package com.zhigaras.home.presentation

import androidx.core.os.bundleOf
import com.zhigaras.calls.domain.CallRoutes
import com.zhigaras.calls.domain.model.DisputeParty
import com.zhigaras.calls.domain.model.ReadyToCallUser
import com.zhigaras.cloudservice.CloudService
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.core.ProvideUserId
import com.zhigaras.home.databinding.FragmentHomeBinding
import com.zhigaras.home.domain.HomeCloudService
import com.zhigaras.home.domain.HomeCommunication
import com.zhigaras.home.domain.NavigateToCall
import com.zhigaras.home.domain.model.HomeSubject

class HomeViewModel(
    private val navigateToCall: NavigateToCall,
    private val provideUserId: ProvideUserId,
    override val communication: HomeCommunication.Mutable,
    homeCloudService: HomeCloudService,
    dispatchers: Dispatchers
) : BaseViewModel<FragmentHomeBinding, HomeUiState>(dispatchers) {
    
    private val callback = object : CloudService.Callback<List<HomeSubject>> {
        override fun provide(data: List<HomeSubject>) {
            communication.postBackground(HomeUiState.NewSubjectList(data))
        }
        
        override fun error(message: String) {
            communication.postBackground(HomeUiState.Error(message))
        }
    }
    
    init {
        homeCloudService.subscribeToSubjects(callback)
    }
    
    fun navigateToCall(subjectId: Int, disputeParty: DisputeParty) {
        val user = ReadyToCallUser(provideUserId.provide(), subjectId, disputeParty)
        navigateToCall.navigateToCall(bundleOf(CallRoutes.READY_TO_CALL_USER_KEY to user))
    }
}