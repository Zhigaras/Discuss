package com.zhigaras.home.presentation.home

import android.view.View
import android.widget.Toast
import com.zhigaras.adapterdelegate.CompositeAdapter
import com.zhigaras.core.NetworkState
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
}

interface HomeNetworkUiState : HomeUiState {
    fun matches(networkState: NetworkState): Boolean
    
    class Available : HomeNetworkUiState {
        override fun matches(networkState: NetworkState) = networkState is NetworkState.Available
        
        override fun update(binding: FragmentHomeBinding) {
            binding.networkStateView.visibility = View.GONE
        }
    }
    
    class Loosing : HomeNetworkUiState {
        override fun matches(networkState: NetworkState) = networkState is NetworkState.Loosing
        
        override fun update(binding: FragmentHomeBinding) {
        
        }
    }
    
    class Lost : HomeNetworkUiState {
        override fun matches(networkState: NetworkState) = networkState is NetworkState.Lost
        
        override fun update(binding: FragmentHomeBinding) {
            binding.networkStateView.visibility = View.VISIBLE
            binding.networkStateView.text = "Lost connection"
            
        }
    }
    
    class Unavailable : HomeNetworkUiState {
        override fun matches(networkState: NetworkState) = networkState is NetworkState.Unavailable
        
        override fun update(binding: FragmentHomeBinding) {
            binding.networkStateView.visibility = View.VISIBLE
            binding.networkStateView.text = "Connection unavailable"
        }
    }
}