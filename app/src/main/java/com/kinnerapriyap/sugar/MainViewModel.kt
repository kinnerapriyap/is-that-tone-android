package com.kinnerapriyap.sugar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.kinnerapriyap.sugar.data.GameCardInfo
import com.kinnerapriyap.sugar.data.GameRoom

const val ROOMS_COLLECTION = "rooms"
const val PLAYERS_KEY = "players"
const val IS_STARTED_KEY = "isStarted"
const val ROUNDS_INFO_KEY = "roundsInfo"
const val ACTIVE_ROUND_KEY = "activeRound"

class MainViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val roomDocument: DocumentReference?
        get() {
            val roomName = roomName.value
            return if (roomName != null && roomName.isNotBlank()) {
                db.collection(ROOMS_COLLECTION).document(roomName)
            } else null
        }

    private val _roomName = MutableLiveData("")
    val roomName: LiveData<String> = _roomName
    fun onRoomNameChanged(newName: String) {
        _roomName.value = newName
    }

    private val _userName = MutableLiveData("")
    val userName: LiveData<String> = _userName
    fun onUserNameChanged(newName: String) {
        _userName.value = newName
    }

    private val _uid = MutableLiveData<String?>(null)
    val uid: LiveData<String?> = _uid
    fun onUidChanged(uid: String?) {
        _uid.value = uid
    }

    private val _gameCardInfo = MutableLiveData(GameCardInfo())
    val gameCardInfo: LiveData<GameCardInfo> = _gameCardInfo

    fun enterRoom(openGameCard: () -> Unit) {
        (roomDocument ?: return).get()
            .addOnSuccessListener { doc ->
                val gameRoom = doc.toObject<GameRoom>()
                when {
                    gameRoom == null -> createRoom(openGameCard)
                    !gameRoom.isStarted -> joinRoom(gameRoom, openGameCard)
                    else -> {
                        // TODO: Show room is occupied
                    }
                }
            }
            .addOnFailureListener {
                // TODO: Handle error
            }
    }

    private fun joinRoom(gameRoom: GameRoom, openGameCard: () -> Unit) {
        if (gameRoom.isStarted && gameRoom.players?.containsKey(uid.value) == false) {
            // TODO: Tell user they cannot join game if it already started
            return
        }
        val players = gameRoom.players?.toMutableMap()?.apply {
            putIfAbsent(uid.value, userName.value)
        }
        (roomDocument ?: return)
            .update(mapOf(PLAYERS_KEY to players))
            .addOnSuccessListener {
                goToGameCard(openGameCard)
            }
            .addOnFailureListener {
                // TODO: Handle error
            }
    }

    private fun createRoom(openGameCard: () -> Unit) {
        val newRoom = GameRoom(
            activePlayer = uid.value,
            players = mapOf(uid.value to userName.value)
        )
        (roomDocument ?: return)
            .set(newRoom)
            .addOnSuccessListener {
                goToGameCard(openGameCard)
            }
            .addOnFailureListener {
                // TODO: Handle error
            }
    }

    private fun goToGameCard(openGameCard: () -> Unit) {
        (roomDocument ?: return)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    // TODO: Handle error
                    return@addSnapshotListener
                }

                val gameRoom = snapshot.toObject<GameRoom>()
                val answers =
                    if (gameRoom?.roundsInfo == null) {
                        (1..(gameRoom?.players?.count() ?: 0))
                            .toList()
                            .map { it.toString() to null }
                            .toMap()
                    } else {
                        gameRoom.roundsInfo.mapValues {
                            uid.value?.let { uid ->
                                it.value.getOrDefault(uid, null)
                            }
                        }
                    }

                _gameCardInfo.value = GameCardInfo(
                    answers = answers,
                    isStarted = gameRoom?.isStarted ?: false,
                    isActivePlayer = gameRoom?.activePlayer == uid.value
                )
            }
        openGameCard.invoke()
    }

    fun startGame(openWordCard: () -> Unit) {
        val answers = gameCardInfo.value?.answers ?: return
        (roomDocument ?: return)
            .update(
                mapOf(
                    IS_STARTED_KEY to true,
                    ACTIVE_ROUND_KEY to 1,
                    ROUNDS_INFO_KEY to
                            answers.map { it.key to emptyMap<String, String>() }.toMap()
                )
            )
            .addOnSuccessListener {
                openWordCard.invoke()
            }
            .addOnFailureListener {
                // TODO: Handle error
            }
    }
}