package com.zhigaras.adapterdelegate

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class ViewHolderDelegate<in M : ListItem>(
    private val binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root) {
    
    abstract fun bind(item: M)
    
    fun bind(payload: Payload<ViewBinding>) {
        payload.bindPayload(binding)
    }
}