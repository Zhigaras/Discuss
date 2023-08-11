package com.zhigaras.login.domain

import com.zhigaras.auth.UserDto

class ShowId : UserDto.Mapper<String> {
    
    override fun map(id: String, email: String, name: String): String {
        return id
    }
}