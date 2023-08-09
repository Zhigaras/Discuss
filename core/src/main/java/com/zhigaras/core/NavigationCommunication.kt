package com.zhigaras.core

interface NavigationCommunication {
    
    interface Observe : Communication.Observe<Screen>
    interface Post : Communication.Post<Screen>
    interface Mutable : Post, Observe
    class Base : Communication.Single<Screen>(), Mutable
}