package com.zhigaras.core

interface NetworkCommunication {
    interface Observe : Communication.Observe<NetworkState>
    interface ObserveForever : Communication.ObserveForever<NetworkState>
    interface Post : Communication.Post<NetworkState>
    interface Mutable : Communication.Mutable<NetworkState>, Post, Observe
    class Base : Communication.Regular<NetworkState>(), Mutable
}