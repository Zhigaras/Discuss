package com.zhigaras.adapterdelegate

import androidx.recyclerview.widget.DiffUtil

internal class DelegateDiffUtilCallback : DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem.areItemTheSame(newItem)
    }
    
    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem.areContentTheSame(newItem)
    }
    
    override fun getChangePayload(oldItem: ListItem, newItem: ListItem): Payload<*> {
        return oldItem.payload(newItem)
    }
}