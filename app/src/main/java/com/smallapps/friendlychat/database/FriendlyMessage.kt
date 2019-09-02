package com.smallapps.friendlychat.database

// Simple data class, representing one message in Database
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

// Simple data class, representing one message in local
data class FriendlyMessageDomain(
    val text: String? = null,
    val name: String? = null,
    val photoUrl: String? = null,
    val key: String? = null
)


