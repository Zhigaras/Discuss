package com.zhigaras.home.data

import com.zhigaras.cloudservice.CloudService
import com.zhigaras.cloudservice.CloudService.Companion.SUBJECTS_PATH
import com.zhigaras.home.domain.HomeCloudService
import com.zhigaras.home.domain.model.HomeSubject

class HomeCloudServiceImpl(private val cloudService: CloudService) : HomeCloudService {
    
    override fun subscribeToSubjects(callback: CloudService.Callback<List<HomeSubject>>) {
        cloudService.subscribeToListMultipleLevels(callback, HomeSubject::class.java, SUBJECTS_PATH)
    }
}