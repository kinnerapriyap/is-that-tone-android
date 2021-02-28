package com.kinnerapriyap.sugar.data

data class GameCardInfo(
    // roundAsString to answer
    val answers: Map<String, String?> = emptyMap(),
    val isStarted: Boolean = false,
    val isActivePlayer: Boolean = false
)
