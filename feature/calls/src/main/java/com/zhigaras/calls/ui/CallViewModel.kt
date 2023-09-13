package com.zhigaras.calls.ui

import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.calls.domain.CallCommunication
import com.zhigaras.webrtc.databinding.FragmentCallBinding

class CallViewModel(
    communication: CallCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<FragmentCallBinding, CallUiState>(communication, dispatchers) {


}