package com.zhigaras.messaging.domain

import com.zhigaras.core.StateFlux

interface DataChannelStateFlux {
    interface Observe : StateFlux.Observe<String>
    interface Post : StateFlux.Post<String>
    interface Mutable : Post, Observe
    class Base : StateFlux.Base<String>(""), Mutable
}
