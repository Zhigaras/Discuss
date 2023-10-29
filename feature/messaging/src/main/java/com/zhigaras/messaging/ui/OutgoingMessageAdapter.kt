package com.zhigaras.messaging.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhigaras.adapterdelegate.DelegateAdapter
import com.zhigaras.adapterdelegate.Payload
import com.zhigaras.messaging.databinding.OutgoingMessageItemBinding
import com.zhigaras.messaging.domain.model.Message
import com.zhigaras.messaging.domain.model.MessageType

class OutgoingMessageAdapter :
    DelegateAdapter<Message.Outgoing, OutgoingMessageAdapter.OutgoingMessageViewHolder> {
    
    override fun viewType() = MessageType.OUTGOING.ordinal
    
    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return OutgoingMessageViewHolder(
            OutgoingMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    
    override fun bindViewHolder(item: Message.Outgoing, viewHolder: OutgoingMessageViewHolder) {
        viewHolder.bind(item)
    }
    
    override fun bindViewHolder(
        item: Message.Outgoing,
        viewHolder: OutgoingMessageViewHolder,
        payload: Payload
    ) {
        viewHolder.bind(item)
    }
    
    inner class OutgoingMessageViewHolder(
        private val binding: OutgoingMessageItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(item: Message.Outgoing) {
            binding.text.text = item.text
        }
    }
}