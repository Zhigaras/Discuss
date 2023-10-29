package com.zhigaras.adapterdelegate

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class DelegateAdapter<M : ListItem, in VH : DelegateViewHolder<M>> {
    
    abstract fun viewType(): Int
    abstract fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun bindViewHolder(item: M, viewHolder: VH) {
        viewHolder.bind(item)
    }
    
    fun bindViewHolder(viewHolder: VH, payload: Payload<ViewBinding>) {
        viewHolder.bind(payload)
    }
}