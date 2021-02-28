package com.kinnerapriyap.sugar.data

data class Room(
    val roundsInfo: Map<Int, Map<String, Int>>? = null,
    val activeRound: Int? = null,
    val players: Map<String?, String?>? = null,
    @field:JvmField // use this annotation if your Boolean field is prefixed with 'is'
    val isStarted: Boolean? = null
)
