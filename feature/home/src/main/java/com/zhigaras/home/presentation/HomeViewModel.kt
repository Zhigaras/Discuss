package com.zhigaras.home.presentation

import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.home.domain.HomeInteractor
import com.zhigaras.home.domain.model.Subject

class HomeViewModel(
    dispatchers: Dispatchers,
    communication: HomeCommunication.Mutable,
    private val homeInteractor: HomeInteractor
) : BaseViewModel<HomeUiState>(communication, dispatchers) {
    
    fun startObservingSubjects(callback: CloudService.Callback<Subject>) {
        homeInteractor.subscribeToSubjects(callback)
    }
    
}