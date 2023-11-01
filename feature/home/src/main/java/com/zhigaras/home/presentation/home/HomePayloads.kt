package com.zhigaras.home.presentation.home

import com.zhigaras.adapterdelegate.Payload
import com.zhigaras.home.databinding.TopicItemBinding

interface HomePayload : Payload<TopicItemBinding>

class TitleChanged(private val newTitle: String) : HomePayload {
    override fun bindPayload(binding: TopicItemBinding) {
        binding.topicDescription.text = newTitle
    }
}

class SupportListSizeChanged(private val newListSize: Int) : HomePayload {
    override fun bindPayload(binding: TopicItemBinding) {
        binding.supportCount.text = newListSize.toString()
    }
}

class AgainstListSizeChanged(private val newListSize: Int) : HomePayload {
    override fun bindPayload(binding: TopicItemBinding) {
        binding.againstCount.text = newListSize.toString()
    }
}