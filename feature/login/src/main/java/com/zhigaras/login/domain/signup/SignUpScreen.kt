package com.zhigaras.login.domain.signup

import android.os.Bundle
import com.zhigaras.core.Screen
import com.zhigaras.login.presentation.signup.SignUpFragment

class SignUpScreen(args: Bundle? = null) :
    Screen.ReplaceAndAddToBackstack(SignUpFragment::class.java, args)