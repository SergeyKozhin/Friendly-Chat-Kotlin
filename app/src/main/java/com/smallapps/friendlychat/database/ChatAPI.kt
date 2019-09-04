package com.smallapps.friendlychat.database

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

// Api for interacting with database
class ChatAPI(private val app: Application) {
    // Reference to database
    private val dataBaseReference = FirebaseDatabase.getInstance().reference

    //Reference to storage
    private val storageReference = FirebaseStorage.getInstance().reference

    // List of messages received
    private val _messages = MutableLiveData<MutableList<FriendlyMessageDomain>>()
    val messages: LiveData<MutableList<FriendlyMessageDomain>>
        get() = _messages

    // Listener for new messages incoming
    private val messageListener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {
            Toast.makeText(app.applicationContext, p0.message, Toast.LENGTH_SHORT).show()
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            val newMessage = p0.getValue(FriendlyMessageDataBase::class.java)
            val key = p0.key
            newMessage?.let {
                val ind = messages.value?.indexOfFirst {
                    it.key == key
                }
                if (ind != -1 && ind != null) {
                    _messages.value = messages.value!!.apply {
                        set(ind, newMessage.toDomain(key))
                    }
                }
            }
        }

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            val message = p0.getValue(FriendlyMessageDataBase::class.java)
            message?.let {
                _messages.value = messages.value!!.apply {
                    add(it.toDomain(p0.key))
                }
            }
        }

        override fun onChildRemoved(p0: DataSnapshot) {
            val newMessage = p0.getValue(FriendlyMessageDataBase::class.java)
            val key = p0.key
            newMessage?.let {
                val ind = messages.value?.indexOfFirst {
                    it.key == key
                }
                if (ind != -1 && ind != null) {
                    _messages.value = messages.value!!.apply {
                        removeAt(ind)
                    }
                }
            }
        }

    }
    private val authListener = FirebaseAuth.AuthStateListener {
        if (it.currentUser != null) {
            setMessageListener()
        } else {
            detachMessageListener()
        }
    }

    init {
        FirebaseAuth.getInstance().addAuthStateListener(authListener)
        _messages.value = mutableListOf()
    }

    fun sendMessage(message: FriendlyMessageDataBase) {
        dataBaseReference.child("messages")
            .push()
            .setValue(message)
    }

    fun uploadImage(imgUri: Uri): Task<Uri> {
        val imgRef =
            storageReference.child("chat_photos").child(imgUri.lastPathSegment!!)
        val uploadTask = imgRef.putFile(imgUri)
        return uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
            if (!it.isSuccessful) {
                it.exception?.let {
                    throw it
                }
            }
            return@Continuation imgRef.downloadUrl
        })
    }

    private fun setMessageListener() {
        dataBaseReference.child("messages").addChildEventListener(messageListener)
    }

    private fun detachMessageListener() {
        dataBaseReference.child("messages").removeEventListener(messageListener)
        _messages.value = mutableListOf()
    }
     fun detachAuthListener() {
         FirebaseAuth.getInstance().removeAuthStateListener(authListener)
     }
}