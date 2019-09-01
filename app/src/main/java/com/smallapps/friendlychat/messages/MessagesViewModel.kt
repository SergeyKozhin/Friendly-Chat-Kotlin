package com.smallapps.friendlychat.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// ViewModel for main messages screen
class MessagesViewModel : ViewModel() {

    // Constants
    companion object {
        const val ANONYMOUS = "anonymous"
        const val MESSAGE_LENGTH = 1000
    }

    // Current username
    val username = ANONYMOUS

    // List of messages
    private val _friendlyMessages = MutableLiveData<List<FriendlyMessage>>(null)
    val friendlyMessages: LiveData<List<FriendlyMessage>>
        get() = _friendlyMessages

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