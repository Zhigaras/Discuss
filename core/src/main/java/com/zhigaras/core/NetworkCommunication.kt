package com.zhigaras.core

interface NetworkCommunication {
    interface Observe : Communication.Observe<NetworkState>
    interface ObserveForever : Communication.ObserveForever<NetworkState>
    interface Post : Communication.Post<NetworkState>
    interface CurrentState : Communication.CurrentState<NetworkState>
    interface Mutable : Communication.Mutable<NetworkState>, Post, Observe, CurrentState
    class Base : Communication.Regular<NetworkState>(), Mutable
}