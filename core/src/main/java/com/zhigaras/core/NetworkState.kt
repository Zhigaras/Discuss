package com.zhigaras.core

interface NetworkState {
    
    class Available : NetworkState
    
    class Loosing : NetworkState
    
    class Lost : NetworkState
    
    class Unavailable : NetworkState
}