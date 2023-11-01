package com.zhigaras.home.domain

import com.zhigaras.cloudservice.CloudService
import com.zhigaras.home.domain.model.HomeSubject

interface HomeCloudService {
    
    fun subscribeToSubjects(callback: CloudService.Callback<List<HomeSubject>>)
    
    fun removeCallback(callback: CloudService.Callback<List<HomeSubject>>)
}