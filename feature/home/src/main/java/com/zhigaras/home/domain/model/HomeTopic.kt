package com.zhigaras.home.domain.model

import com.zhigaras.adapterdelegate.ListItem
import com.zhigaras.adapterdelegate.Payload
import com.zhigaras.home.presentation.AgainstListSizeChanged
import com.zhigaras.home.presentation.SupportListSizeChanged
import com.zhigaras.home.presentation.TitleChanged

class HomeTopic(
    val id: Int = 0,
    val nameRu: String = "",
    val supportList: Map<String, String> = emptyMap(),
    val againstList: Map<String, String> = emptyMap(),
) : ListItem {
    override fun itemType() = 0
    
    override fun areItemTheSame(other: ListItem): Boolean {
        if (other !is HomeTopic) return false
        return id == other.id
    }
    
    override fun areContentTheSame(other: ListItem): Boolean {
        if (other !is HomeTopic) return false
        if (nameRu != other.nameRu) return false
        if (supportList.size != other.supportList.size) return false
        return againstList.size == other.againstList.size
    }
    
    override fun payload(oldItem: ListItem): Payload<*> {
        if (oldItem !is HomeTopic) return Payload.None()
        return when {
            nameRu != oldItem.nameRu -> return TitleChanged(nameRu)
            supportList.size != oldItem.supportList.size -> SupportListSizeChanged(supportList.size)
            againstList.size != oldItem.againstList.size -> AgainstListSizeChanged(againstList.size)
            else -> super.payload(oldItem)
        }
    }
}