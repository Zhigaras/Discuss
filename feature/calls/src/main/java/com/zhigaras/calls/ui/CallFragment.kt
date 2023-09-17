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

class CallFragment : BaseFragment<FragmentCallBinding>(), MainRepository.Listener {
    
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
        mainRepository.listener = this
        
        mainRepository.subscribeForLatestEvent()
        
        binding.switchCameraButton.setOnClickListener { mainRepository.switchCamera() }
        
        binding.micButton.setOnClickListener { v ->
            if (isMicrophoneMuted) {
                binding.micButton.setImageResource(R.drawable.ic_baseline_mic_off_24)
            } else {
                binding.micButton.setImageResource(R.drawable.ic_baseline_mic_24)
            }
            isMicrophoneMuted = !isMicrophoneMuted
        }
        
        binding.videoButton.setOnClickListener { v ->
            if (isCameraMuted) {
                binding.videoButton.setImageResource(R.drawable.ic_baseline_videocam_off_24)
            } else {
                binding.videoButton.setImageResource(R.drawable.ic_baseline_videocam_24)
            }
            isCameraMuted = !isCameraMuted
        }
        
        binding.endCallButton.setOnClickListener { v ->
            mainRepository.endCall()
            
        }
    }
    
    override fun webrtcConnected() {
        requireActivity().runOnUiThread {
            binding.incomingCallLayout.visibility = View.GONE
            binding.whoToCallLayout.visibility = View.GONE
            binding.callLayout.visibility = View.VISIBLE
        }
    }
    
    override fun webrtcClosed() {
        parentFragmentManager.popBackStack()
    }
    
}