package com.zhigaras.messaging.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhigaras.adapterdelegate.DelegateAdapter
import com.zhigaras.adapterdelegate.Payload
import com.zhigaras.messaging.databinding.IncomingMessageItemBinding
import com.zhigaras.messaging.domain.model.Message
import com.zhigaras.messaging.domain.model.MessageType

class IncomingMessageAdapter :
    DelegateAdapter<Message.Incoming, IncomingMessageAdapter.IncomingMessageViewHolder> {
    
    override fun viewType() = MessageType.INCOMING.ordinal
    
    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return IncomingMessageViewHolder(
            IncomingMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
    
    override fun bindViewHolder(item: Message.Incoming, viewHolder: IncomingMessageViewHolder) {
        viewHolder.bind(item)
    }
    
    override fun bindViewHolder(
        item: Message.Incoming,
        viewHolder: IncomingMessageViewHolder,
        payload: Payload
    ) {
        viewHolder.bind(item)
    }
    
    inner class IncomingMessageViewHolder(
        private val binding: IncomingMessageItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(item: Message.Incoming) {
            binding.text.text = item.text
        }
    }
}