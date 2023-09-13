package com.zhigaras.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.zhigaras.calls.data.MainRepository
import com.zhigaras.calls.webrtc.SuccessCallBack
import com.zhigaras.core.BaseFragment
import com.zhigaras.home.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    
    override fun initBinding(inflater: LayoutInflater) = FragmentHomeBinding.inflate(inflater)
    
    private val viewModel by viewModel<HomeViewModel>()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainRepository = MainRepository
        binding.enterBtn.setOnClickListener { v ->
            
            //login to firebase here
            mainRepository.login(
                binding.username.getText().toString(), requireActivity().application,
                object : SuccessCallBack {
                    override fun onSuccess() {
                        viewModel.navigateToCall()
                    }
                }
            )
        }
        
        
    }
}