package com.zhigaras.home.domain

data class Subject(
    val nameEn: String,
    val nameRu: String,
    val supportList: List<String>,
    val againstList: List<String>,
)
