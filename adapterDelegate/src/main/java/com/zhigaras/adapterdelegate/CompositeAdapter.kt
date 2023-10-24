package com.zhigaras.adapterdelegate

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CompositeAdapter(
    private val delegates: Map<Int, DelegateAdapter<ListItem, RecyclerView.ViewHolder>>
) : ListAdapter<ListItem, RecyclerView.ViewHolder>(DelegateDiffUtilCallback()) {
    
    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates[viewType]?.createViewHolder(parent)
            ?: throw IllegalStateException("Can`t create ViewHolder for view type $viewType")
    }
    
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        delegates[item.itemType()]?.bindViewHolder(item, holder)
            ?: throw IllegalStateException("Can`t bind ViewHolder for position $position")
    }
    
    class Builder {
        
        private val delegates =
            mutableMapOf<Int, DelegateAdapter<ListItem, RecyclerView.ViewHolder>>()
        
        fun addAdapter(adapter: DelegateAdapter<out ListItem, *>): Builder {
            delegates[adapter.viewType()] =
                adapter as DelegateAdapter<ListItem, RecyclerView.ViewHolder>
            return this
        }
        
        fun build(): CompositeAdapter {
            require(delegates.isNotEmpty()) { "Add one adapter at least" }
            return CompositeAdapter(delegates)
        }
    }
}