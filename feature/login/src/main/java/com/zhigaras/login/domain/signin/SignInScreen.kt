package com.zhigaras.login.domain.signin

import com.zhigaras.core.Screen
import com.zhigaras.login.presentation.signin.SignInFragment

object SignInScreen : Screen.ReplaceWithClearBackstack(SignInFragment::class.java)