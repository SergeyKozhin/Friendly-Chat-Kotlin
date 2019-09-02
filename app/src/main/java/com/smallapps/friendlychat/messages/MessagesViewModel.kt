package com.smallapps.friendlychat.messages

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.smallapps.friendlychat.database.ChatAPI
import com.smallapps.friendlychat.database.FriendlyMessage

// ViewModel for main messages screen
class MessagesViewModel(app: Application) : AndroidViewModel(app) {

    // Constants
    companion object {
        const val ANONYMOUS = "anonymous"
        const val MESSAGE_LENGTH = 1000
    }

    private val chatAPI = ChatAPI(getApplication())

    // Current username
    var username: String? = null

    val friendlyMessages = chatAPI.messages

    // Visibility of progress bar
    private val _progressBarVisible = MutableLiveData<Boolean>(false)
    val progressBarVisible: LiveData<Boolean>
        get() = _progressBarVisible

    // Enable/disable send button
    private val _sendButtonEnabled = MutableLiveData<Boolean>(false)
    val sendButtonEnabled: LiveData<Boolean>
        get() = _sendButtonEnabled

    // Sending message state
    private val _sendingMessage = MutableLiveData<Boolean>(false)
    val sendingMessage: LiveData<Boolean>
        get() =  _sendingMessage

    // Picking Image state
    private val _pickingImage = MutableLiveData<Boolean>(false)
    val pickingImage: LiveData<Boolean>
        get() = _pickingImage

    fun initializeChat() {
        username = FirebaseAuth.getInstance().currentUser?.displayName
        chatAPI.setMessageListener()
    }

    fun destroyChat() {
        username = ANONYMOUS
        chatAPI.detachMessageListener()
    }


    fun sendMessage(message: FriendlyMessage) {
        chatAPI.sendMessage(message)
    }

    // Functions for enable/disable send button
    fun enableSendButton() {
        _sendButtonEnabled.value = true
    }

    fun disableSendButton() {
        _sendButtonEnabled.value =false
    }

    // Functions for operating sending message state
    fun onSendMessage() {
        _sendingMessage.value = true
    }

    fun onSendMessageCompleted() {
        _sendingMessage.value = false
    }

    // Functions for operating picking image state
    fun onPickImage() {
        _pickingImage.value = true
    }

    fun onPickImageCompleted() {
        _pickingImage.value = false
    }
}