package com.zhigaras.calls.ui

import android.view.LayoutInflater
import com.zhigaras.core.BaseFragment
import com.zhigaras.webrtc.databinding.FragmentCallBinding

class CallFragment : BaseFragment<FragmentCallBinding>() {
    override fun initBinding(inflater: LayoutInflater) = FragmentCallBinding.inflate(inflater)
    
    
}