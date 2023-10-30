package com.zhigaras.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.zhigaras.adapterdelegate.CompositeAdapter
import com.zhigaras.core.BaseFragment
import com.zhigaras.home.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    
    override fun initBinding(inflater: LayoutInflater) = FragmentHomeBinding.inflate(inflater)
    
    private val viewModel by viewModel<HomeViewModel>()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val adapter = CompositeAdapter.Builder()
            .addAdapter(
                SubjectAdapter { subId, opinion -> viewModel.navigateToCall(subId, opinion) }
            )
            .build()
        binding.subjectsRv.adapter = adapter
        viewModel.observe(this) {
            it.update(binding)
        }
    }
}