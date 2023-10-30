package com.zhigaras.home.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhigaras.adapterdelegate.DelegateAdapter
import com.zhigaras.adapterdelegate.DelegateViewHolder
import com.zhigaras.calls.domain.model.DisputeParty
import com.zhigaras.home.databinding.SubjectItemBinding
import com.zhigaras.home.domain.model.HomeSubject

class SubjectAdapter(
    private val onClick: (Int, DisputeParty) -> Unit
) : DelegateAdapter<HomeSubject, SubjectAdapter.SubjectViewHolder>() {
    
    inner class SubjectViewHolder(
        private val binding: SubjectItemBinding
    ) : DelegateViewHolder<HomeSubject>(binding) {
        
        override fun bind(item: HomeSubject) {
            binding.subjectDescription.text = item.nameRu
            binding.supportCount.text = item.supportList.size.toString()
            binding.againstCount.text = item.againstList.size.toString()
            binding.supportButton.setOnClickListener {
                onClick.invoke(item.id, DisputeParty.SUPPORT)
            }
            binding.againstButton.setOnClickListener {
                onClick.invoke(item.id, DisputeParty.AGAINST)
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
}