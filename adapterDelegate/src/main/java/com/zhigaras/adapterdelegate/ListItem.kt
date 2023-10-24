package com.zhigaras.adapterdelegate

interface ListItem {
    
    fun itemType(): Int
    
    fun areItemTheSame(other: Any): Boolean
    
    fun areContentTheSame(other: Any): Boolean
}