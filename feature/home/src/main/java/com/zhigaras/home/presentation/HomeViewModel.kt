package com.zhigaras.home.presentation

import android.os.Bundle
import com.zhigaras.cloudservice.CloudService
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.home.databinding.FragmentHomeBinding
import com.zhigaras.home.domain.HomeCloudService
import com.zhigaras.home.domain.HomeCommunication
import com.zhigaras.home.domain.NavigateToCall
import com.zhigaras.home.domain.model.HomeSubject

class HomeViewModel(
    private val navigateToCall: NavigateToCall,
    override val communication: HomeCommunication.Mutable,
    homeCloudService: HomeCloudService,
    dispatchers: Dispatchers
) : BaseViewModel<FragmentHomeBinding, HomeUiState>(dispatchers), NavigateToCall {
    
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
    
    override fun navigateToCall(args: Bundle?) = navigateToCall.navigateToCall(args)
}