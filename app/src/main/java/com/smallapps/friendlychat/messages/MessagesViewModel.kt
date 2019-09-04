package com.smallapps.friendlychat.messages

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.smallapps.friendlychat.R
import com.smallapps.friendlychat.database.ChatAPI
import com.smallapps.friendlychat.database.FriendlyMessageDataBase

// ViewModel for main messages screen
class MessagesViewModel(private val app: Application) : AndroidViewModel(app) {

    // Constants
    companion object {
        const val ANONYMOUS = "anonymous"
        const val MESSAGE_LENGTH = 1000
    }

    private val chatAPI = ChatAPI(getApplication())

    private val _currentMessageText = MutableLiveData<String>()
    val currentMessageText: LiveData<String>
        get() = _currentMessageText

    private val _currentMessageUrl = MutableLiveData<String>()
    val currentMessageUrl: LiveData<String>
        get() = _currentMessageUrl

    var currentMessageImgHeight: Int? = null
    var currentMessageImgWidth: Int? = null

    // Current username
    private val username: String
        get() = FirebaseAuth.getInstance().currentUser?.displayName ?: ANONYMOUS

    val friendlyMessages = chatAPI.messages

    // Visibility of progress bar
    private val _progressBarVisible = MutableLiveData<Boolean>(false)
    val progressBarVisible: LiveData<Boolean>
        get() = _progressBarVisible

    // Sending message state
    private val _sendingMessage = MutableLiveData<Boolean>(false)
    val sendingMessage: LiveData<Boolean>
        get() = _sendingMessage

    // Picking Image state
    private val _pickingImage = MutableLiveData<Boolean>(false)
    val pickingImage: LiveData<Boolean>
        get() = _pickingImage

    override fun onCleared() {
        chatAPI.detachAuthListener()
    }

    fun sendMessage() {
        chatAPI.sendMessage(
            FriendlyMessageDataBase(
                currentMessageText.value,
                username,
                currentMessageUrl.value,
                currentMessageImgHeight,
                currentMessageImgWidth
            )
        )
        _currentMessageText.value = null
        _currentMessageUrl.value = null
    }

    fun uploadImage(img: Uri?) {
        img?.let {
            val urlTask = chatAPI.uploadImage(img)
            urlTask.addOnCompleteListener {
                Toast.makeText(
                    app.applicationContext,
                    app.getString(R.string.image_upload_success),
                    Toast.LENGTH_SHORT
                ).show()
                _currentMessageUrl.value = it.result.toString()
            }.addOnFailureListener {
                Toast.makeText(
                    app.applicationContext,
                    "${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Functions for operating sending message state
    fun onSendMessage() {
        _sendingMessage.value = true
    }

    fun onSendMessageCompleted() {
        _sendingMessage.value = false
    }

    fun setMessageText(text: String?) {
        _currentMessageText.value = text
    }

    // Functions for operating picking image state
    fun onPickImage() {
        _pickingImage.value = true
    }

    fun onPickImageCompleted() {
        _pickingImage.value = false
    }
}