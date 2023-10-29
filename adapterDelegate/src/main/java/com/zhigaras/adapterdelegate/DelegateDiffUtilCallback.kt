package com.zhigaras.adapterdelegate

import androidx.recyclerview.widget.DiffUtil

internal class DelegateDiffUtilCallback : DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return newItem.areItemTheSame(oldItem)
    }
    
    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return newItem.areContentTheSame(oldItem)
    }
    
    override fun getChangePayload(oldItem: ListItem, newItem: ListItem): Payload<*> {
        return oldItem.payload(newItem)
    }
}