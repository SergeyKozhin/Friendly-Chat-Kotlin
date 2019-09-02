package com.smallapps.friendlychat.database

import com.google.firebase.database.FirebaseDatabase

object ChatAPI {
    private val dataBaseReference = FirebaseDatabase.getInstance().reference

    val messagesReference = dataBaseReference.child("messages")

    fun sendMessage(message: FriendlyMessage) {
        messagesReference
            .push()
            .setValue(message)
    }
}