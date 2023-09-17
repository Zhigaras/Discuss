package com.zhigaras.calls.domain.model

class Subject(
    val nameEn: String = "",
    val nameRu: String = "",
    val supportList: Map<String, String> = emptyMap(),
    val againstList: Map<String, String> = emptyMap(),
)
