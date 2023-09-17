package com.zhigaras.calls.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.zhigaras.calls.data.CallsControllerImpl
import com.zhigaras.calls.domain.CallsController
import com.zhigaras.core.BaseFragment
import com.zhigaras.webrtc.databinding.FragmentCallBinding

class CallFragment : BaseFragment<FragmentCallBinding>() {
    
    override fun initBinding(inflater: LayoutInflater) = FragmentCallBinding.inflate(inflater)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callsControllerImpl: CallsController = CallsControllerImpl(requireContext().applicationContext)
        binding.callBtn.setOnClickListener {
//        start a call request here
            callsControllerImpl.startNegotiation("uzZAvzvRrFNoZz1p2xCrsdmpt4T2")
        }
        callsControllerImpl.initLocalView(binding.localView)
        callsControllerImpl.initRemoteView(binding.remoteView)
        
        callsControllerImpl.subscribeToConnectionEvents(FirebaseAuth.getInstance().uid.toString())
        
        
    }
}