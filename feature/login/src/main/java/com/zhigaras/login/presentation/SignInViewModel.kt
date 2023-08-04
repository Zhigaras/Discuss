package com.zhigaras.login.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhigaras.auth.Auth
import com.zhigaras.auth.UserDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel(
    private val auth: Auth
) : ViewModel() {
    
    val liveData = MutableLiveData<UserDto>()
    
    fun signIn(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = auth.signInWithEmailAndPassword(email, password)
            liveData.postValue(result)
        }
    }
}