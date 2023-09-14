package com.zhigaras.calls.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.zhigaras.calls.data.MainRepository
import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.calls.domain.model.ConnectionDataType
import com.zhigaras.calls.webrtc.ErrorCallBack
import com.zhigaras.calls.webrtc.NewEventCallBack
import com.zhigaras.core.BaseFragment
import com.zhigaras.webrtc.R
import com.zhigaras.webrtc.databinding.FragmentCallBinding

class CallFragment : BaseFragment<FragmentCallBinding>(), MainRepository.Listener {
    
    var mainRepository = MainRepository
    
    private var isCameraMuted = false
    private var isMicrophoneMuted = false
    
    override fun initBinding(inflater: LayoutInflater) = FragmentCallBinding.inflate(inflater)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.callBtn.setOnClickListener { v ->
            //start a call request here
            mainRepository.sendCallRequest(
                "uzZAvzvRrFNoZz1p2xCrsdmpt4T2", object : ErrorCallBack {
                    override fun onError() {
                        Toast.makeText(
                            requireContext(),
                            "couldnt find the target",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
        mainRepository.initLocalView(binding.localView)
        mainRepository.initRemoteView(binding.remoteView)
        mainRepository.listener = this
        
        mainRepository.subscribeForLatestEvent(object : NewEventCallBack {
            override fun onNewEventReceived(data: ConnectionData) {
                if (data.type === ConnectionDataType.START_CALL) {
                    requireActivity().runOnUiThread {
                        binding.incomingNameTV.text = data.sender + " is Calling you"
                        binding.incomingCallLayout.setVisibility(View.VISIBLE)
                        binding.acceptButton.setOnClickListener { v ->
                            //star the call here
                            mainRepository.startCall(data.sender)
                            binding.incomingCallLayout.visibility = View.GONE
                        }
                        binding.rejectButton.setOnClickListener { v ->
                            binding.incomingCallLayout.visibility = View.GONE
                        }
                    }
                }
            }
        })
        
        binding.switchCameraButton.setOnClickListener { mainRepository.switchCamera() }
        
        binding.micButton.setOnClickListener { v ->
            if (isMicrophoneMuted) {
                binding.micButton.setImageResource(R.drawable.ic_baseline_mic_off_24)
            } else {
                binding.micButton.setImageResource(R.drawable.ic_baseline_mic_24)
            }
            mainRepository.toggleAudio(isMicrophoneMuted)
            isMicrophoneMuted = !isMicrophoneMuted
        }
        
        binding.videoButton.setOnClickListener { v ->
            if (isCameraMuted) {
                binding.videoButton.setImageResource(R.drawable.ic_baseline_videocam_off_24)
            } else {
                binding.videoButton.setImageResource(R.drawable.ic_baseline_videocam_24)
            }
            mainRepository.toggleVideo(isCameraMuted)
            isCameraMuted = !isCameraMuted
        }
        
        binding.endCallButton.setOnClickListener { v ->
            mainRepository.endCall()
            
        }
    }
    
    override fun webrtcConnected() {
        requireActivity().runOnUiThread(Runnable {
            binding.incomingCallLayout.setVisibility(View.GONE)
            binding.whoToCallLayout.setVisibility(View.GONE)
            binding.callLayout.setVisibility(View.VISIBLE)
        })
    }
    
    override fun webrtcClosed() {
        parentFragmentManager.popBackStack()
    }
    
}