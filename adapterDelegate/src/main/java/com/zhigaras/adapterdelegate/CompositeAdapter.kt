package com.zhigaras.adapterdelegate

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class CompositeAdapter(
    private val delegates: Map<Int, DelegateAdapter<ListItem, DelegateViewHolder<ListItem>>>
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
        delegates[item.itemType()]?.bindViewHolder(item, holder as DelegateViewHolder<ListItem>)
            ?: throw IllegalStateException("Can`t bind ViewHolder for position $position")
    }
    
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val item = getItem(position)
        val delegate = delegates[item.itemType()]
            ?: throw IllegalStateException("Can`t bind ViewHolder for position $position")
        if (payloads.isNotEmpty())
            delegate.bindViewHolder(
                holder as DelegateViewHolder<ListItem>,
                payloads.last() as Payload<ViewBinding>
            )
        else delegate.bindViewHolder(item, holder as DelegateViewHolder<ListItem>)
    }
    
    class Builder {
        
        private val delegates =
            mutableMapOf<Int, DelegateAdapter<ListItem, DelegateViewHolder<ListItem>>>()
        
        fun addAdapter(adapter: DelegateAdapter<out ListItem, *>): Builder {
            delegates[adapter.viewType()] =
                adapter as DelegateAdapter<ListItem, DelegateViewHolder<ListItem>>
            return this
        }
        
        fun build(): CompositeAdapter {
            require(delegates.isNotEmpty()) { "Add one adapter at least" }
            return CompositeAdapter(delegates)
        }
    }
}