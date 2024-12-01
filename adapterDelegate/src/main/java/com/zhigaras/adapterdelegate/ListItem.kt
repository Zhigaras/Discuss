package com.zhigaras.adapterdelegate

interface ListItem {
    
    fun itemType(): Int = this::class.hashCode()
    
    fun areItemTheSame(newItem: ListItem): Boolean
    
    fun areContentTheSame(newItem: ListItem): Boolean = this == newItem
    
    fun payload(newItem: ListItem): Payload<*> = Payload.None()
}