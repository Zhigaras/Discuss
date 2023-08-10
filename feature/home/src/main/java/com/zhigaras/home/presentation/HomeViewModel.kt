package com.zhigaras.home.presentation

import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers

class HomeViewModel(
    dispatchers: Dispatchers,
    communication: HomeCommunication.Mutable,
    private val cloudService: CloudService
) : BaseViewModel<HomeUiState>(communication, dispatchers) {
    

}