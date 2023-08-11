package com.zhigaras.home.domain

import com.zhigaras.core.Screen
import com.zhigaras.home.presentation.HomeFragment

object HomeScreen : Screen.ReplaceWithClearBackstack(HomeFragment::class.java)