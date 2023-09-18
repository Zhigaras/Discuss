package com.zhigaras.calls.domain

import android.os.Bundle
import com.zhigaras.calls.ui.CallFragment
import com.zhigaras.core.Screen

class CallScreen(args: Bundle? = null) :
    Screen.ReplaceAndAddToBackstack(CallFragment::class.java, args)