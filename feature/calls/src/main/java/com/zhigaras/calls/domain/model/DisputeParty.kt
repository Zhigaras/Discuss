package com.zhigaras.calls.domain.model

enum class DisputeParty(val path: String) {
    SUPPORT("supportList") {
        override fun opposite() = AGAINST
    },
    AGAINST("againstList") {
        override fun opposite() = SUPPORT
    };
    
    abstract fun opposite(): DisputeParty
}