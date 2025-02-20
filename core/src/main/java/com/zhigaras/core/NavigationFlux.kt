package com.zhigaras.core

interface NavigationFlux {
    interface Observe : SingleEventFlux.Observe<Screen>
    interface Post : SingleEventFlux.Post<Screen>
    interface Mutable : Post, Observe
    class Base : SingleEventFlux.Base<Screen>(), Mutable
}
