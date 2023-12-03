package com.zhigaras.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.zhigaras.adapterdelegate.CompositeAdapter
import com.zhigaras.core.BaseFragment
import com.zhigaras.home.databinding.FragmentHomeBinding
import com.zhigaras.home.presentation.suggesttopic.SuggestTopicBottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    
    override fun initBinding(inflater: LayoutInflater) = FragmentHomeBinding.inflate(inflater)
    
    private val viewModel by viewModel<HomeViewModel>()
    private val permissions = Permissions()
    
    private val launcher = registerForActivityResult(CustomPermissionsContract()) {
        it.handle(requireContext(), viewModel)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val adapter = CompositeAdapter.Builder()
            .addAdapter(TopicAdapter { topicId, opinion ->
                run { permissions.check(requireContext(), launcher, viewModel, topicId, opinion) }
            })
            .build()
        binding.topicsRv.adapter = adapter
        viewModel.observe(this) {
            it.update(binding)
        }
        
        binding.suggestTopicButton.setOnClickListener {
            SuggestTopicBottomSheetDialog().show(parentFragmentManager, null)
        }
        
        binding.toProfileButton.setOnClickListener {
            viewModel.navigateToProfile()
        }
    }
}