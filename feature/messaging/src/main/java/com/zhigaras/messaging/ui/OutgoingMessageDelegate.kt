package com.zhigaras.messaging.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zhigaras.adapterdelegate.DelegateAdapter
import com.zhigaras.adapterdelegate.ViewHolderDelegate
import com.zhigaras.messaging.databinding.OutgoingMessageItemBinding
import com.zhigaras.messaging.domain.model.Message
import com.zhigaras.messaging.domain.model.MessageType

class OutgoingMessageDelegate :
    DelegateAdapter<Message.Outgoing, OutgoingMessageDelegate.OutgoingMessageViewHolder>() {
    
    inner class OutgoingMessageViewHolder(
        private val binding: OutgoingMessageItemBinding
    ) : ViewHolderDelegate<Message.Outgoing>(binding) {
        
        override fun bind(item: Message.Outgoing) {
            binding.text.text = item.text
        }
    }
    
    override fun viewType() = MessageType.OUTGOING.ordinal
    
    override fun createViewHolder(parent: ViewGroup): ViewHolderDelegate<Message.Outgoing> {
        return OutgoingMessageViewHolder(
            OutgoingMessageItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}