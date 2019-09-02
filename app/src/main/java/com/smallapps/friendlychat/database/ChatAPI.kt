package com.smallapps.friendlychat.database

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

// Api for interacting with database
class ChatAPI(private val app: Application) {
    // Reference to database
    private val dataBaseReference = FirebaseDatabase.getInstance().reference

    // List of messages received
    private val _messages = MutableLiveData<List<FriendlyMessage>>()
    val messages: LiveData<List<FriendlyMessage>>
        get() = _messages

    // Listener for new messages incoming
    private val messageListener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {
            Toast.makeText(app.applicationContext, "Error: ${p0.message}", Toast.LENGTH_SHORT).show()
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            val message = p0.getValue(FriendlyMessage::class.java)
            message?.let {
                if (_messages.value == null) {
                    _messages.value = listOf(message)
                } else {
                    _messages.value = listOf(message) + messages.value!!
                }
                Log.i("Chat", "Added: ${message.text}")
            }}

        override fun onChildRemoved(p0: DataSnapshot) {
        }
    }

    fun sendMessage(message: FriendlyMessage) {
        dataBaseReference.child("messages")
            .push()
            .setValue(message)
    }

    fun setMessageListener() {
        Log.i("Chat", "Listener attached")
        dataBaseReference.child("messages").addChildEventListener(messageListener)
    }

    fun detachMessageListener() {
        dataBaseReference.child("messages").removeEventListener(messageListener)
        _messages.value  = listOf()
    }
}