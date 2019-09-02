package com.smallapps.friendlychat.database

import java.nio.channels.spi.AbstractSelectionKey

// Simple data class, representing one message
data class FriendlyMessageDataBase(
    val text: String? = null,
    val name: String? = null,
    val photoUrl: String? = null){

    fun toDomain(key: String?): FriendlyMessageDomain {
        return FriendlyMessageDomain(
            text, name, photoUrl, key
        )
    }
}

data class FriendlyMessageDomain(
    val text: String? = null,
    val name: String? = null,
    val photoUrl: String? = null,
    val key: String? = null
)


