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
import com.kinnerapriyap.sugar.data.WordCard
import com.kinnerapriyap.sugar.data.WordCardInfo

const val ROOMS_COLLECTION = "rooms"
const val ROUNDS_INFO_KEY = "roundsInfo"
const val ACTIVE_ROUND_KEY = "activeRound"
const val PLAYERS_KEY = "players"
const val IS_STARTED_KEY = "isStarted"
const val WORD_CARDS_KEY = "wordCards"

const val WORD_CARDS_COLLECTION = "wordCards"
const val LANGUAGE_KEY = "language"

const val MAX_PLAYERS = 4

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

    private var _uid: String? = null
    fun onUidChanged(uid: String?) {
        _uid = uid
    }

    private var _gameRoom: GameRoom? = null

    private val _gameCardInfo = MutableLiveData(GameCardInfo())
    val gameCardInfo: LiveData<GameCardInfo> = _gameCardInfo

    private val maxRounds: Int
        get() = _gameCardInfo.value?.answers?.size ?: 0

    private var _wordCards: List<WordCard>? = null

    private var _wordCardInfo = MutableLiveData(WordCardInfo())
    val wordCardInfo: LiveData<WordCardInfo> = _wordCardInfo

    fun enterRoom(openGameCard: () -> Unit) {
        (roomDocument ?: return).get()
            .addOnSuccessListener { doc ->
                val gameRoom = doc.toObject<GameRoom>()
                when {
                    gameRoom == null ->
                        createRoom(openGameCard)
                    gameRoom.players?.containsKey(_uid) == true ->
                        rejoinRoom(openGameCard)
                    !gameRoom.isStarted && MAX_PLAYERS > gameRoom.players?.count() ?: 0 ->
                        joinRoom(gameRoom, openGameCard)
                    else -> {
                        // TODO: Show room is occupied or player count is full
                    }
                }
            }
            .addOnFailureListener {
                // TODO: Handle error
            }
    }

    private fun rejoinRoom(openGameCard: () -> Unit) = goToGameCard(openGameCard)

    private fun joinRoom(gameRoom: GameRoom, openGameCard: () -> Unit) {
        val players = gameRoom.players?.toMutableMap()?.apply {
            putIfAbsent(_uid, userName.value)
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
            activePlayer = _uid,
            players = mapOf(_uid to userName.value)
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
                _gameRoom = gameRoom
                val answers =
                    if (gameRoom?.roundsInfo == null) {
                        (1..(gameRoom?.players?.count() ?: 0))
                            .toList()
                            .map { it.toString() to null }
                            .toMap()
                    } else {
                        gameRoom.roundsInfo.mapValues {
                            _uid?.let { uid ->
                                it.value.getOrDefault(uid, null)
                            }
                        }
                    }
                _wordCardInfo.value =
                    _wordCardInfo.value?.copy(
                        wordCard = gameRoom?.activeRound?.let { gameRoom.wordCards?.get(it - 1) },
                        usedAnswers = answers.values.filterNotNull()
                    )

                _gameCardInfo.value = GameCardInfo(
                    answers = answers,
                    activeRound = gameRoom?.activeRound,
                    isStarted = gameRoom?.isStarted ?: false,
                    isActivePlayer = gameRoom?.activePlayer == _uid
                )
            }
        openGameCard.invoke()
    }

    fun startGame(openWordCard: () -> Unit) {
        db.collection(WORD_CARDS_COLLECTION)
            .whereEqualTo(LANGUAGE_KEY, "tl")
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.documents.shuffle()
                _wordCards =
                    querySnapshot.documents
                        .take(maxRounds)
                        .mapNotNull { it.toObject<WordCard>() }

                initialiseRoom(openWordCard)
            }
            .addOnFailureListener {
                // TODO: Handle error
            }
    }

    private fun initialiseRoom(openWordCard: () -> Unit) {
        val answers = gameCardInfo.value?.answers ?: return
        (roomDocument ?: return)
            .update(
                mapOf(
                    IS_STARTED_KEY to true,
                    ACTIVE_ROUND_KEY to 1,
                    ROUNDS_INFO_KEY to
                            answers.map { it.key to emptyMap<String, String>() }.toMap(),
                    WORD_CARDS_KEY to _wordCards
                )
            )
            .addOnSuccessListener {
                openWordCard.invoke()
            }
            .addOnFailureListener {
                // TODO: Handle error
            }
    }

    fun setAnswer(selectedAnswer: String, navigateBack: () -> Unit) {
        val newRoundsInfo =
            _gameRoom?.roundsInfo?.mapValues { roundInfo ->
                if (roundInfo.key.toInt() == _gameRoom?.activeRound) {
                    roundInfo.value.toMutableMap().apply {
                        _uid?.let { put(it, selectedAnswer) }
                    }
                } else roundInfo.value
            }
        (roomDocument ?: return)
            .update(mapOf(ROUNDS_INFO_KEY to newRoundsInfo))
            .addOnSuccessListener {
                navigateBack.invoke()
            }
            .addOnFailureListener {
                // TODO: Handle error
            }
    }
}