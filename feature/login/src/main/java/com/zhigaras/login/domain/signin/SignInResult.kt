package com.zhigaras.login.domain.signin

import androidx.annotation.StringRes
import com.zhigaras.auth.UserDto
import com.zhigaras.home.domain.SaveUserToCloud
import com.zhigaras.login.domain.NavigateToHome
import com.zhigaras.login.domain.ShowId
import com.zhigaras.login.domain.UserMapper
import com.zhigaras.login.presentation.signin.SignInUiState

interface SignInResult {
    
    suspend fun handle(
        communication: SignInCommunication.Post,
        saveUserToCloud: SaveUserToCloud,
        navigateToHome: NavigateToHome
    )
    
    class Success(
        private val user: UserDto,
        private val showId: ShowId = ShowId(),
        private val userMapper: UserMapper = UserMapper()
    ) : SignInResult {
        
        override suspend fun handle(
            communication: SignInCommunication.Post,
            saveUserToCloud: SaveUserToCloud,
            navigateToHome: NavigateToHome
        ) {
            saveUserToCloud.save(user.map(showId), user.map(userMapper))
            navigateToHome.navigateToHome()
        }
    }
    
    object OneTapSignInLaunched : SignInResult {
        
        override suspend fun handle(
            communication: SignInCommunication.Post,
            saveUserToCloud: SaveUserToCloud,
            navigateToHome: NavigateToHome
        ) {
        }
    }
    
    class Error(@StringRes private val errorId: Int) : SignInResult {
        
        override suspend fun handle(
            communication: SignInCommunication.Post,
            saveUserToCloud: SaveUserToCloud,
            navigateToHome: NavigateToHome
        ) {
            communication.postUi(SignInUiState.SingleEventError(errorId))
        }
    }
}