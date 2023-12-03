package com.zhigaras.messaging.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhigaras.adapterdelegate.DelegateAdapter
import com.zhigaras.adapterdelegate.DelegateViewHolder
import com.zhigaras.messaging.databinding.OutgoingMessageItemBinding
import com.zhigaras.messaging.domain.model.Message
import com.zhigaras.messaging.domain.model.MessageType

class OutgoingMessageDelegate :
    DelegateAdapter<Message.Outgoing, OutgoingMessageDelegate.OutgoingMessageViewHolder>() {
    
    inner class OutgoingMessageViewHolder(
        private val binding: OutgoingMessageItemBinding
    ) : DelegateViewHolder<Message.Outgoing>(binding) {
        
        override fun bind(item: Message.Outgoing) {
            binding.text.text = item.text
        }
    }
    
    override fun viewType() = MessageType.OUTGOING.ordinal
    
    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return OutgoingMessageViewHolder(
            OutgoingMessageItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}