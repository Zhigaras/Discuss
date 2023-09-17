package com.zhigaras.home.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.home.databinding.FragmentHomeBinding
import com.zhigaras.calls.domain.model.DisputePosition
import com.zhigaras.home.domain.HomeCommunication
import com.zhigaras.calls.domain.HomeInteractor
import com.zhigaras.home.domain.NavigateToCall
import com.zhigaras.calls.domain.model.Subject
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeInteractor: HomeInteractor,
    private val navigateToCall: NavigateToCall,
    dispatchers: Dispatchers,
    communication: HomeCommunication.Mutable
) : BaseViewModel<FragmentHomeBinding, HomeUiState>(communication, dispatchers), NavigateToCall {
    
    fun startObservingSubjects(callback: CloudService.Callback<Subject>) {
//        homeInteractor.subscribeToSubjects(callback) // TODO: navigate right here???
    }
    
    fun addUserToWaitList(userId: String, subjectId: String, userOpinion: DisputePosition) {
        viewModelScope.launch {
            homeInteractor.addUserToWaitList(subjectId, userId, userOpinion)
            homeInteractor.checkMatching(subjectId, userId, userOpinion).let {
                Log.d("AAA", it.toString())
//                it.updateUi(homeInteractor)
            }
        }
    }
    
    override fun navigateToCall() = navigateToCall.navigateToCall()
}