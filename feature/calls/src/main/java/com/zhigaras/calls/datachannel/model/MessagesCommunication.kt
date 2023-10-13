package com.zhigaras.calls.datachannel.model

import com.zhigaras.core.Communication

interface MessagesCommunication {
    
    interface Observe : Communication.Observe<List<Message>>
    interface Post : Communication.Post<List<Message>>
    interface Mutable : Communication.Mutable<List<Message>>, Post, Observe
    class Base : Communication.Regular<List<Message>>(), Mutable
}