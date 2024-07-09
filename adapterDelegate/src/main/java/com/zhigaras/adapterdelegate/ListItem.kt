package com.zhigaras.adapterdelegate

interface ListItem {
    
    fun itemType(): Int
    
    fun areItemTheSame(other: ListItem): Boolean
    
    fun areContentTheSame(other: ListItem): Boolean
    
    fun payload(oldItem: ListItem): Payload<*> = Payload.None()
}