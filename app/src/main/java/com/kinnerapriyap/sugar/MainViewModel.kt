package com.kinnerapriyap.sugar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.kinnerapriyap.sugar.data.GameRoom

const val ROOMS_COLLECTION = "rooms"

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

    fun enterRoom(openGameCard: () -> Unit) {
        (roomDocument ?: return).get()
            .addOnSuccessListener { doc ->
                val room = doc.toObject<GameRoom>()
                when {
                    room == null -> createRoom(openGameCard)
                    !room.isStarted -> joinRoom(room, openGameCard)
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
        val players = gameRoom.players?.toMutableMap()?.apply {
            putIfAbsent(uid.value, userName.value)
        }
        (roomDocument ?: return)
            .update(mapOf("players" to players))
            .addOnSuccessListener {
                openGameCard.invoke()
            }
            .addOnFailureListener {
                // TODO: Handle error
            }
    }

    private fun createRoom(openGameCard: () -> Unit) {
        val newRoom = GameRoom(
            currentPlayer = uid.value,
            players = mapOf(uid.value to userName.value)
        )
        (roomDocument ?: return)
            .set(newRoom)
            .addOnSuccessListener {
                openGameCard.invoke()
            }
            .addOnFailureListener {
                // TODO: Handle error
            }
    }
}