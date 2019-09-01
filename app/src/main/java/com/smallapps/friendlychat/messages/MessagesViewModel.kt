package com.smallapps.friendlychat.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

const val ANONYMOUS = "anonymous"
const val MESSAGE_LENGTH = 1000

class MessagesViewModel : ViewModel() {

    val username = ANONYMOUS

    private val _friendlyMessages = MutableLiveData<List<FriendlyMessage>>(null)
    val friendlyMessages: LiveData<List<FriendlyMessage>>
        get() = _friendlyMessages

    private val _progressBarVisible = MutableLiveData<Boolean>(null)
    val progressBarVisible: LiveData<Boolean>
        get() = _progressBarVisible

    private val _sendButtonEnabled = MutableLiveData<Boolean>(null)
    val sendButtonEnabled: LiveData<Boolean>
        get() = _sendButtonEnabled

    private val _sendingMessage = MutableLiveData<Boolean>(false)
    val sendingMessage: LiveData<Boolean>
        get() =  _sendingMessage

    private val _pickingImage = MutableLiveData<Boolean>(false)
    val pickingImage: LiveData<Boolean>
        get() = _pickingImage


    fun showProgressbar() {
        _progressBarVisible.value = true
    }

    fun enableSendButton() {
        _sendButtonEnabled.value = true
    }

    fun disableSendButton() {
        _sendButtonEnabled.value =false
    }

    fun onSendMessage() {
        _sendingMessage.value = true
    }

    fun onPickImage() {
        _pickingImage.value = true
    }

    fun hideProgressBar() {
        _progressBarVisible.value =false
    }

    fun onSendMessageCompleted() {
        _sendingMessage.value = false
    }

    fun onPickImageCompleted() {
        _pickingImage.value = false
    }
}