package com.zhigaras.login.presentation

import android.view.LayoutInflater
import com.zhigaras.core.presentation.BaseFragment
import com.zhigaras.login.databinding.FragmentSignInBinding

class SignInFragment : BaseFragment<FragmentSignInBinding>() {
    
    override fun initBinding(inflater: LayoutInflater) = FragmentSignInBinding.inflate(inflater)
}