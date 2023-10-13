package com.zhigaras.messaging.domain

import com.zhigaras.core.Communication
import com.zhigaras.messaging.domain.model.Message

interface MessagesCommunication {
    
    interface Observe : Communication.Observe<List<Message>>
    interface Post : Communication.Post<List<Message>>
    interface Mutable : Communication.Mutable<List<Message>>, Post, Observe
    class Base : Communication.Regular<List<Message>>(), Mutable
}