package com.zhigaras.login.domain

import com.zhigaras.auth.UserDto
import com.zhigaras.home.domain.model.User

class UserMapper : UserDto.Mapper<User> {
    
    override fun map(id: String, email: String, name: String): User {
        return User(name, email, "empty")
    }
}