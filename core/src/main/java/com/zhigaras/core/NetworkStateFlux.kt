package com.zhigaras.core

interface NetworkStateFlux {
    interface Observe : StateFlux.Observe<NetworkState>
    interface Post : StateFlux.Post<NetworkState>
    interface Mutable : StateFlux.Mutable<NetworkState>, Post, Observe
    class Base : StateFlux.Base<NetworkState>(NetworkState.Initial()), Mutable
}
