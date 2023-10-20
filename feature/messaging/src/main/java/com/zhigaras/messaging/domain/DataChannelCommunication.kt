package com.zhigaras.messaging.domain

import com.zhigaras.core.Communication

interface DataChannelCommunication {
    
    interface Observe : Communication.Observe<String>
    interface Post : Communication.Post<String>
    interface Mutable : Post, Observe
    class Base : Communication.Single<String>(), Mutable
}