package com.zhigaras.core

abstract class DiscussException : Throwable() {
    
    abstract fun errorId(): Int
}