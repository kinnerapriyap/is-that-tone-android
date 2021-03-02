/*
 * Copyright 2021 Kinnera Priya Putti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
