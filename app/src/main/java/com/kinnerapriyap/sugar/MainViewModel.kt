package com.kinnerapriyap.sugar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

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
}