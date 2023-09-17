package com.zhigaras.calls.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.zhigaras.calls.data.CallsControllerImpl
import com.zhigaras.calls.domain.CallsController
import com.zhigaras.core.BaseFragment
import com.zhigaras.webrtc.databinding.FragmentCallBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CallFragment : BaseFragment<FragmentCallBinding>() {
    
    private val viewModel by viewModel<CallViewModel>()
    
    override fun initBinding(inflater: LayoutInflater) = FragmentCallBinding.inflate(inflater)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.callBtn.setOnClickListener {
//        start a call request here
            viewModel.startCall("uzZAvzvRrFNoZz1p2xCrsdmpt4T2")
        }
        viewModel.init(binding)
        
    }
}