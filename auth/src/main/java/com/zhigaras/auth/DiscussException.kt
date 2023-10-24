package com.zhigaras.auth

abstract class DiscussException : Throwable() {
    
    abstract fun errorId(): Int
}