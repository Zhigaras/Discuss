package com.zhigaras.home.presentation

import android.view.LayoutInflater
import com.zhigaras.core.BaseFragment
import com.zhigaras.home.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    
    override fun initBinding(inflater: LayoutInflater) = FragmentHomeBinding.inflate(inflater)
    
    
}