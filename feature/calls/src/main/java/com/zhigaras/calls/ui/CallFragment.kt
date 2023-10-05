package com.zhigaras.calls.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.zhigaras.calls.domain.CallRoutes
import com.zhigaras.calls.domain.model.DisputeParty
import com.zhigaras.core.BaseFragment
import com.zhigaras.webrtc.databinding.FragmentCallBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CallFragment : BaseFragment<FragmentCallBinding>() {
    
    private val viewModel by viewModel<CallViewModel>()
    
    override fun initBinding(inflater: LayoutInflater) = FragmentCallBinding.inflate(inflater)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel.init(binding.localView, binding.remoteView) // TODO: fix
        arguments?.let {
            val disputePosition = it.getString(CallRoutes.DISPUTE_POSITION_KEY) ?: return@let
            val opinion = DisputeParty.valueOf(disputePosition)
            viewModel.lookForOpponent("1", opinion)
        }
        
        viewModel.observe(this) {
            it.update(binding)
        }
    }
}