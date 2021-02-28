package com.kinnerapriyap.sugar.data

data class GameRoom(
    val roundsInfo: Map<Int, Map<String, Int>>? = null,
    val activeRound: Int = 1,
    val players: Map<String?, String?>? = null,
    val currentPlayer: String? = null,
    @field:JvmField // use this annotation if your Boolean field is prefixed with 'is'
    val isStarted: Boolean = false
)
