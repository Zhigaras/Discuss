package com.zhigaras.adapterdelegate

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding

class CompositeAdapter(
    private val delegates: Map<Int, DelegateAdapter<ListItem, ViewHolderDelegate<ListItem>>>
) : ListAdapter<ListItem, ViewHolderDelegate<ListItem>>(DelegateDiffUtilCallback()) {
    
    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDelegate<ListItem> {
        return delegates[viewType]?.createViewHolder(parent)
            ?: throw IllegalStateException("Can`t create ViewHolder for view type $viewType")
    }
    
    override fun onBindViewHolder(holder: ViewHolderDelegate<ListItem>, position: Int) {
        val item = getItem(position)
        delegates[item.itemType()]?.bindViewHolder(item, holder)
            ?: throw IllegalStateException("Can`t bind ViewHolder for position $position")
    }
    
    override fun onBindViewHolder(
        holder: ViewHolderDelegate<ListItem>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val item = getItem(position)
        val delegate = delegates[item.itemType()]
            ?: throw IllegalStateException("Can`t bind ViewHolder for position $position")
        if (payloads.isNotEmpty())
            delegate.bindViewHolder(holder, payloads.last() as Payload<ViewBinding>)
        else delegate.bindViewHolder(item, holder)
    }
    
    class Builder {
        
        private val delegates =
            mutableMapOf<Int, DelegateAdapter<ListItem, ViewHolderDelegate<ListItem>>>()
        
        fun addDelegate(adapter: DelegateAdapter<out ListItem, *>): Builder {
            delegates[adapter.viewType()] =
                adapter as DelegateAdapter<ListItem, ViewHolderDelegate<ListItem>>
            return this
        }
        
        fun build(): CompositeAdapter {
            require(delegates.isNotEmpty()) { "Add one adapter at least" }
            return CompositeAdapter(delegates)
        }
    }
}