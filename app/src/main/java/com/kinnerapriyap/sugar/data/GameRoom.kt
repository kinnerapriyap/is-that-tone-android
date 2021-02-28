package com.kinnerapriyap.sugar.data

data class GameRoom(
    // (roundAsString to (uid to answer))
    val roundsInfo: Map<String, Map<String, String?>>? = null,
    val activeRound: Int? = null,
    // uid to userName
    val players: Map<String?, String?>? = null,
    val activePlayer: String? = null,
    @field:JvmField // use this annotation if your Boolean field is prefixed with 'is'
    val isStarted: Boolean = false
)
