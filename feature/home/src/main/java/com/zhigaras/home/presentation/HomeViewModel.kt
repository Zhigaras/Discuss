package com.zhigaras.home.presentation

import com.zhigaras.calls.domain.MatchingInteractor
import com.zhigaras.calls.domain.model.Subject
import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.home.databinding.FragmentHomeBinding
import com.zhigaras.home.domain.HomeCommunication
import com.zhigaras.home.domain.NavigateToCall

class HomeViewModel(
    private val matchingInteractor: MatchingInteractor,
    private val navigateToCall: NavigateToCall,
    dispatchers: Dispatchers,
    communication: HomeCommunication.Mutable
) : BaseViewModel<FragmentHomeBinding, HomeUiState>(communication, dispatchers), NavigateToCall {
    
    fun startObservingSubjects(callback: CloudService.Callback<Subject>) {
//        matchingInteractor.subscribeToSubjects(callback) // TODO: navigate right here???
    }
    
    override fun navigateToCall() = navigateToCall.navigateToCall()
}