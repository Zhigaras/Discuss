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
    override fun areItemTheSame(newItem: ListItem): Boolean {
        if (newItem !is HomeTopic) return false
        return id == newItem.id
    }
    
    override fun payload(newItem: ListItem): Payload<*> {
        if (newItem !is HomeTopic) return Payload.None()
        return when {
            nameRu != newItem.nameRu -> return TitleChanged(nameRu)
            supportList.size != newItem.supportList.size -> SupportListSizeChanged(newItem.supportList.size)
            againstList.size != newItem.againstList.size -> AgainstListSizeChanged(newItem.againstList.size)
            else -> super.payload(newItem)
        }
    }
}