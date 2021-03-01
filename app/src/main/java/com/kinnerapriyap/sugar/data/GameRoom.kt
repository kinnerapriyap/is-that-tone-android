package com.kinnerapriyap.sugar.data

data class GameRoom(
    // (roundAsString to (uid to answer))
    val roundsInfo: Map<String, Map<String, String?>> = emptyMap(),
    val activeRound: Int = 0,
    // uid to userName
    val players: List<Player> = emptyList(),
    @field:JvmField // use this annotation if your Boolean field is prefixed with 'is'
    val isStarted: Boolean = false,
    val wordCards: List<WordCard> = emptyList()
) {
    fun isActivePlayer(uid: String) =
        players.getOrNull(if (activeRound == 0) 0 else activeRound - 1)?.uid == uid
}
