package com.zhigaras.login.presentation.signup.domain

import com.zhigaras.auth.UserDto
import com.zhigaras.home.domain.SaveUserToCloud
import com.zhigaras.login.domain.NavigateToHome
import com.zhigaras.login.domain.ShowId
import com.zhigaras.login.domain.UserMapper
import com.zhigaras.login.presentation.signup.SignUpUiState

interface SignUpResult {
    
    suspend fun handle(
        communication: SignUpCommunication.Post,
        navigateToHome: NavigateToHome,
        saveUserToCloud: SaveUserToCloud
    )
    
    class Success(
        private val user: UserDto,
        private val showId: ShowId = ShowId(),
        private val userMapper: UserMapper = UserMapper()
    ) : SignUpResult {
        override suspend fun handle(
            communication: SignUpCommunication.Post,
            navigateToHome: NavigateToHome,
            saveUserToCloud: SaveUserToCloud
        ) {
            saveUserToCloud.save(user.map(showId), user.map(userMapper))
            navigateToHome.navigateToHome()
        }
    }
    
    class Error(private val errorId: Int) : SignUpResult {
        override suspend fun handle(
            communication: SignUpCommunication.Post,
            navigateToHome: NavigateToHome,
            saveUserToCloud: SaveUserToCloud
        ) {
            communication.postUi(SignUpUiState.SingleEventError(errorId))
        }
    }
}