package com.zhigaras.adapterdelegate

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface DelegateAdapter<M : ListItem, in VH : RecyclerView.ViewHolder> {
    
    fun viewType(): Int
    fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun bindViewHolder(model: M, viewHolder: VH)
}