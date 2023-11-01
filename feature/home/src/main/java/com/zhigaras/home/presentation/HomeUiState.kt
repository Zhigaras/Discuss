package com.zhigaras.home.presentation

import android.widget.Toast
import com.zhigaras.adapterdelegate.CompositeAdapter
import com.zhigaras.core.UiState
import com.zhigaras.home.databinding.FragmentHomeBinding
import com.zhigaras.home.domain.model.HomeTopic

interface HomeUiState : UiState<FragmentHomeBinding> {
    
    class NewTopicList(private val list: List<HomeTopic>) : HomeUiState {
        
        override fun update(binding: FragmentHomeBinding) {
            (binding.topicsRv.adapter as CompositeAdapter).submitList(list)
        }
    }
    
    class Error(private val message: String) : HomeUiState,
        UiState.SingleEvent<FragmentHomeBinding>() {
        
        override val block: FragmentHomeBinding.() -> Unit = {
            Toast.makeText(root.context, message, Toast.LENGTH_SHORT).show()
        }
    }
    
    class SuggestSuccessfullySent : HomeUiState {
        
        override fun update(binding: FragmentHomeBinding) {
            TODO("Not yet implemented")
        }
    }
    
    class SuggestSendingFailed(private val msg: String?) : HomeUiState {
        
        override fun update(binding: FragmentHomeBinding) {
            TODO("Not yet implemented")
        }
    }
}