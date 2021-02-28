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

    // (roundNo to answer)
    private val _rounds = MutableLiveData<Map<Int, String?>>(emptyMap())
    val rounds: LiveData<Map<Int, String?>> = _rounds

    private val _isStarted = MutableLiveData(false)
    val isStarted: LiveData<Boolean> = _isStarted

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
        val players = gameRoom.players?.toMutableMap()?.apply {
            putIfAbsent(uid.value, userName.value)
        }
        (roomDocument ?: return)
            .update(mapOf("players" to players))
            .addOnSuccessListener {
                goToGameCard(openGameCard)
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
                _isStarted.value = gameRoom?.isStarted ?: false
                val currentMaxRounds = gameRoom?.players?.count() ?: 0
                _rounds.value = (1..currentMaxRounds).toList().map { it to null }.toMap()
            }
        openGameCard.invoke()
    }

    fun startGame() {

    }
}