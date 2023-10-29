package com.zhigaras.home.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhigaras.adapterdelegate.DelegateAdapter
import com.zhigaras.adapterdelegate.DelegateViewHolder
import com.zhigaras.home.databinding.SubjectItemBinding
import com.zhigaras.home.domain.model.HomeSubject

class SubjectAdapter : DelegateAdapter<HomeSubject, SubjectAdapter.SubjectViewHolder>() {
    
    inner class SubjectViewHolder(
        private val binding: SubjectItemBinding
    ) : DelegateViewHolder<HomeSubject>(binding) {
        
        override fun bind(item: HomeSubject) {
            binding.subjectDescription.text = item.nameRu
            binding.onlineTextView.text = item.supportList.size.toString()
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
}