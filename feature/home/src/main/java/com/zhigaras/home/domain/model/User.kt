package com.zhigaras.home.domain.model

class User(
    val name: String,
    val email: String,
    val connectionData: ConnectionData = ConnectionData()
)

class ConnectionData(val interruptedByOpponent: Boolean = false)