package com.zhigaras.home.domain.model

import com.zhigaras.adapterdelegate.ListItem
import com.zhigaras.adapterdelegate.Payload
import com.zhigaras.home.presentation.AgainstListSizeChanged
import com.zhigaras.home.presentation.SupportListSizeChanged
import com.zhigaras.home.presentation.TitleChanged

data class HomeTopic(
    val id: Int = 0,
    val nameRu: String = "",
    val supportList: Map<String, String> = emptyMap(),
    val againstList: Map<String, String> = emptyMap(),
) : ListItem {
    override fun areItemTheSame(newItem: ListItem): Boolean {
        if (newItem !is HomeTopic) return false
        return id == newItem.id
    }
    
    override fun payload(newItem: ListItem): List<Payload<*>> {
        if (newItem !is HomeTopic) return emptyList()
        val payloads = mutableListOf<Payload<*>>()
        when {
            nameRu != newItem.nameRu -> payloads.add(TitleChanged(nameRu))
            supportList.size != newItem.supportList.size -> payloads.add(SupportListSizeChanged(newItem.supportList.size))
            againstList.size != newItem.againstList.size -> payloads.add(AgainstListSizeChanged(newItem.againstList.size))
            else -> super.payload(newItem)
        }
        return payloads
    }
}