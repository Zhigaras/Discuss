package com.zhigaras.messaging.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhigaras.messaging.databinding.MessageItemBinding
import com.zhigaras.messaging.domain.model.Message

class MessagesAdapter : RecyclerView.Adapter<MessagesViewHolder>() {
    
    private var data = listOf<Message>()
    
    @SuppressLint("NotifyDataSetChanged")
    fun setData(messages: List<Message>) {
        data = messages
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        return MessagesViewHolder(MessageItemBinding.inflate(LayoutInflater.from(parent.context)))
    }
    
    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }
    
    override fun getItemCount() = data.size
}

class MessagesViewHolder(private val binding: MessageItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    
    fun bind(message: Message) {
        binding.messageTextView.text = message.text
    }
}