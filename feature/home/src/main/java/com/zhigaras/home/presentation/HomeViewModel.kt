package com.zhigaras.home.presentation

import android.os.Bundle
import com.zhigaras.calls.domain.model.Subject
import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.home.databinding.FragmentHomeBinding
import com.zhigaras.home.domain.HomeCommunication
import com.zhigaras.home.domain.NavigateToCall

class HomeViewModel(
    private val navigateToCall: NavigateToCall,
    override val communication: HomeCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<FragmentHomeBinding, HomeUiState>(dispatchers), NavigateToCall {
    
    fun startObservingSubjects(callback: CloudService.Callback<Subject>) {
//        matchingInteractor.subscribeToSubjects(callback) // TODO: navigate right here???
    }
    
    override fun navigateToCall(args: Bundle?) = navigateToCall.navigateToCall(args)
}