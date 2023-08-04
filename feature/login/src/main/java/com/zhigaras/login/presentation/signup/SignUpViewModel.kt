package com.zhigaras.login.presentation.signup

import com.zhigaras.core.presentation.BaseViewModel
import com.zhigaras.core.presentation.Communication

class SignUpViewModel(
    private val communication: Communication.Mutable<SignUpState> = Communication.Base()
) : BaseViewModel<SignUpState>(communication) {


}