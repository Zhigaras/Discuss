package com.zhigaras.login.domain

import com.zhigaras.auth.UserDto
import com.zhigaras.home.domain.SaveUserToCloud
import com.zhigaras.login.presentation.signup.SignUpUiState

interface AuthResult {
    
    suspend fun handle(
        communication: SignUpCommunication.Post,
        navigateToHome: NavigateToHome,
        saveUserToCloud: SaveUserToCloud
    )
    
    class Success(
        private val user: UserDto,
        private val showId: ShowId = ShowId(),
        private val userMapper: UserMapper = UserMapper()
    ) : AuthResult {
        override suspend fun handle(
            communication: SignUpCommunication.Post,
            navigateToHome: NavigateToHome,
            saveUserToCloud: SaveUserToCloud
        ) {
            saveUserToCloud.save(user.map(showId), user.map(userMapper))
            navigateToHome.navigateToHome()
        }
    }
    
    class Error(private val errorId: Int) : AuthResult {
        override suspend fun handle(
            communication: SignUpCommunication.Post,
            navigateToHome: NavigateToHome,
            saveUserToCloud: SaveUserToCloud
        ) {
            communication.postUi(SignUpUiState.SingleEventError(errorId))
        }
    }
}