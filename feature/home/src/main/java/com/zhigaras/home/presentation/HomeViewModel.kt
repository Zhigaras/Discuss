package com.zhigaras.home.presentation

import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers

class HomeViewModel(
    dispatchers: Dispatchers,
    communication: HomeCommunication.Mutable
) : BaseViewModel<HomeUiState>(communication, dispatchers) {


}