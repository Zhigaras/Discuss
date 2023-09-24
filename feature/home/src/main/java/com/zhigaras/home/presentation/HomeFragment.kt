package com.zhigaras.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import com.zhigaras.calls.domain.CallRoutes
import com.zhigaras.core.BaseFragment
import com.zhigaras.home.R
import com.zhigaras.home.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    
    override fun initBinding(inflater: LayoutInflater) = FragmentHomeBinding.inflate(inflater)
    
    private val viewModel by viewModel<HomeViewModel>()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.enterButton.setOnClickListener {
            val opinion = if (binding.opinionChipGroup.checkedChipId == R.id.against_chip) "AGAINST"
            else "SUPPORT"
            viewModel.navigateToCall(bundleOf(CallRoutes.DISPUTE_POSITION_KEY to opinion))
        }
    }
}