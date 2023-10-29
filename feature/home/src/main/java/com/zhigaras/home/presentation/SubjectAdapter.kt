package com.zhigaras.home.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhigaras.adapterdelegate.DelegateAdapter
import com.zhigaras.adapterdelegate.Payload
import com.zhigaras.home.databinding.SubjectItemBinding
import com.zhigaras.home.domain.model.HomeSubject

class SubjectAdapter : DelegateAdapter<HomeSubject, SubjectAdapter.SubjectViewHolder> {
    
    inner class SubjectViewHolder(
        private val binding: SubjectItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(item: HomeSubject) {
            binding.subjectDescription.text = item.nameRu
        }
        
        fun bind(payload: Payload) {
            when (payload) {
                is TitleChanged -> binding.subjectDescription.text = payload.newTitle
                is SupportListSizeChanged -> binding.onlineTextView.text =
                    payload.newListSize.toString()
            }
        }
    }
    
    override fun viewType() = 0
    
    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return SubjectViewHolder(
            SubjectItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
    
    override fun bindViewHolder(
        item: HomeSubject,
        viewHolder: SubjectViewHolder,
        payload: Payload
    ) {
        viewHolder.bind(payload)
    }
    
    override fun bindViewHolder(item: HomeSubject, viewHolder: SubjectViewHolder) {
        viewHolder.bind(item)
    }
}