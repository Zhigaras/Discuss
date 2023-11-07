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
        if (againstList.size != other.againstList.size) return false
        return true
    }
    
    override fun payload(other: ListItem): Payload<*> {
        if (other !is HomeTopic) return Payload.None()
        return when {
            nameRu != other.nameRu -> return TitleChanged(other.nameRu)
            supportList.size != other.supportList.size -> SupportListSizeChanged(other.supportList.size)
            againstList.size != other.againstList.size -> AgainstListSizeChanged(other.supportList.size)
            else -> super.payload(other)
        }
    }
}