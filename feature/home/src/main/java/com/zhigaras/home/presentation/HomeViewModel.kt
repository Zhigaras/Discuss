package com.zhigaras.home.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.home.domain.DisputePosition
import com.zhigaras.home.domain.HomeInteractor
import com.zhigaras.home.domain.model.Subject
import kotlinx.coroutines.launch

class HomeViewModel(
    dispatchers: Dispatchers,
    communication: HomeCommunication.Mutable,
    private val homeInteractor: HomeInteractor
) : BaseViewModel<HomeUiState>(communication, dispatchers) {
    
    fun startObservingSubjects(callback: CloudService.Callback<Subject>) {
        homeInteractor.subscribeToSubjects(callback)
    }
    
    fun addUserToWaitList(userId: String, subjectId: String, userOpinion: DisputePosition) {
        viewModelScope.launch {
            homeInteractor.addUserToWaitList(subjectId, userId, userOpinion)
            homeInteractor.checkMatching(subjectId, userId, userOpinion).let {
                Log.d("AAA", it.toString())
                it.updateUi(homeInteractor)
            }
        }
    }
}