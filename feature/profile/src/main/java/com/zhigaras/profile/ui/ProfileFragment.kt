package com.zhigaras.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.zhigaras.core.BaseFragment
import com.zhigaras.profile.databinding.FragmentProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override fun initBinding(inflater: LayoutInflater) = FragmentProfileBinding.inflate(inflater)
    
    private val viewModel by viewModel<ProfileViewModel>()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
        
        viewModel.observe(this) {
            it.update(binding)
        }
    }
}