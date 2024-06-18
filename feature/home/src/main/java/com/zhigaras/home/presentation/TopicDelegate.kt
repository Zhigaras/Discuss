package com.zhigaras.home.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zhigaras.adapterdelegate.DelegateAdapter
import com.zhigaras.adapterdelegate.ViewHolderDelegate
import com.zhigaras.calls.domain.model.DisputeParty
import com.zhigaras.home.databinding.TopicItemBinding
import com.zhigaras.home.domain.model.HomeTopic

class TopicDelegate(
    private val onClick: (Int, DisputeParty) -> Unit
) : DelegateAdapter<HomeTopic, TopicDelegate.TopicViewHolder>() {
    
    inner class TopicViewHolder(
        private val binding: TopicItemBinding
    ) : ViewHolderDelegate<HomeTopic>(binding) {
        
        override fun bind(item: HomeTopic) {
            binding.topicDescription.text = item.nameRu
            binding.supportCount.text = item.supportList.size.toString()
            binding.againstCount.text = item.againstList.size.toString()
            binding.supportButton.setOnClickListener {
                onClick.invoke(item.id, DisputeParty.SUPPORT)
            }
            binding.againstButton.setOnClickListener {
                onClick.invoke(item.id, DisputeParty.AGAINST)
            }
        }
    }
    
    override fun viewType() = 0
    
    override fun createViewHolder(parent: ViewGroup): ViewHolderDelegate<HomeTopic> {
        return TopicViewHolder(
            TopicItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}