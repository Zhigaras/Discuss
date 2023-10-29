package com.zhigaras.home.presentation

import com.zhigaras.adapterdelegate.Payload
import com.zhigaras.home.databinding.SubjectItemBinding

interface HomePayload : Payload<SubjectItemBinding>

class TitleChanged(private val newTitle: String) : HomePayload {
    override fun bindPayload(binding: SubjectItemBinding) {
        binding.subjectDescription.text = newTitle
    }
}

class SupportListSizeChanged(private val newListSize: Int) : HomePayload {
    override fun bindPayload(binding: SubjectItemBinding) {
        binding.onlineTextView.text = newListSize.toString()
    }
}

class AgainstListSizeChanged(private val newListSize: Int) : HomePayload {
    override fun bindPayload(binding: SubjectItemBinding) {
    
    }
}