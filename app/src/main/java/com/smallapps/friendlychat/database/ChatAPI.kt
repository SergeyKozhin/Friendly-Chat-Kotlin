package com.smallapps.friendlychat.database

import com.google.firebase.database.FirebaseDatabase

object ChatAPI {
    private val dataBaseReference = FirebaseDatabase.getInstance().reference

    fun sendMessage(message: FriendlyMessage) {
        dataBaseReference
            .child("messages")
            .push()
            .setValue(message)
    }
}