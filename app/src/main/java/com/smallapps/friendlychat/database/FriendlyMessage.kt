package com.smallapps.friendlychat.database

// Simple data class, representing one message
data class FriendlyMessage(
    val text: String?,
    val name: String,
    val photoUrl: String?
)
