package com.zhigaras.calls.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.zhigaras.calls.data.MainRepository
import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.calls.domain.model.ConnectionDataType
import com.zhigaras.calls.webrtc.NewEventCallBack
import com.zhigaras.core.BaseFragment
import com.zhigaras.webrtc.R
import com.zhigaras.webrtc.databinding.FragmentCallBinding

class CallFragment : BaseFragment<FragmentCallBinding>() {
    
    private var isCameraMuted = false
    
    private var isMicrophoneMuted = false
    override fun initBinding(inflater: LayoutInflater) = FragmentCallBinding.inflate(inflater)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainRepository = MainRepository(requireContext().applicationContext)
        binding.callBtn.setOnClickListener {
//        start a call request here
            mainRepository.sendCallRequest("uzZAvzvRrFNoZz1p2xCrsdmpt4T2")
        }
        mainRepository.initLocalView(binding.localView)
        mainRepository.initRemoteView(binding.remoteView)
        
        mainRepository.subscribeForLatestEvent()
        
        
    }
}