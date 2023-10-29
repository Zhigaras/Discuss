package com.zhigaras.adapterdelegate

import androidx.viewbinding.ViewBinding

interface Payload<in VB : ViewBinding> {
    
    fun bindPayload(binding: VB)
    
    class None : Payload<ViewBinding> {
        override fun bindPayload(binding: ViewBinding) = Unit
    }
}