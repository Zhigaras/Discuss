package com.zhigaras.calls.domain.model

class Subject(
    val nameEn: String = "",
    val nameRu: String = "",
    val supportList: List<String> = emptyList(),
    val againstList: List<String> = emptyList(),
)
