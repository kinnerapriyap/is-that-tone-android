package com.kinnerapriyap.sugar.data

data class GameCardInfo(
    // roundAsString to answer
    val answers: Map<String, String?> = emptyMap(),
    val activeRound: Int? = null,
    val isStarted: Boolean = false,
    val isActivePlayer: Boolean = false,
    val isRoundOver: Boolean = false,
    val isGameOver: Boolean = false,
)
