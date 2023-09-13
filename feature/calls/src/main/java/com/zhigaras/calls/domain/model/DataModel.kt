package com.zhigaras.calls.domain.model

data class DataModel(
    val target: String,
    val sender: String,
    val data: String?,
    val type: DataModelType
)
