package com.zhigaras.auth

import com.google.firebase.auth.FirebaseUser

class UserDto(user: FirebaseUser) {
    private val id: String = user.uid
    private val email: String = user.email ?: ""
    private val name: String = user.displayName ?: ""
    
    override fun toString(): String {
        return "$id, $email, $name"
    }
}
