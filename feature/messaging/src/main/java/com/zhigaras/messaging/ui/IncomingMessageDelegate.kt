package com.zhigaras.messaging.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zhigaras.adapterdelegate.DelegateAdapter
import com.zhigaras.adapterdelegate.ViewHolderDelegate
import com.zhigaras.messaging.databinding.IncomingMessageItemBinding
import com.zhigaras.messaging.domain.model.Message
import com.zhigaras.messaging.domain.model.MessageType

class IncomingMessageDelegate :
    DelegateAdapter<Message.Incoming, IncomingMessageDelegate.IncomingMessageViewHolder>() {
    
    inner class IncomingMessageViewHolder(
        private val binding: IncomingMessageItemBinding
    ) : ViewHolderDelegate<Message.Incoming>(binding) {
        
        override fun bind(item: Message.Incoming) {
            binding.text.text = item.text
        }
    }
    
    override fun viewType() = MessageType.INCOMING.ordinal
    
    override fun createViewHolder(parent: ViewGroup): ViewHolderDelegate<Message.Incoming> {
        return IncomingMessageViewHolder(
            IncomingMessageItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}