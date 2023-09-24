package com.zhigaras.auth

import com.google.firebase.auth.FirebaseUser

class UserDto(user: FirebaseUser) : Map {
    private val id: String = user.uid
    private val email: String = user.email ?: ""
    private val name: String = user.displayName ?: ""
    
    interface Mapper<T> {
        
        fun map(id: String, email: String, name: String): T
    }
    
    override fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(id, email, name)
    }
}

interface Map {
    
    fun <T> map(mapper: UserDto.Mapper<T>): T
}

