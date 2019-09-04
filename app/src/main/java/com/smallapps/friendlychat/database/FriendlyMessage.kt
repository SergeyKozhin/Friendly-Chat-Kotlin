package com.smallapps.friendlychat.database

// Simple data class, representing one message in Database
data class FriendlyMessageDataBase(
    var text: String? = null,
    var name: String? = null,
    var photoUrl: String? = null,
    var imgHeight: Int? = null,
    var ImgWidth: Int? = null){

    fun toDomain(key: String?): FriendlyMessageDomain {
        return FriendlyMessageDomain(
            text, name, photoUrl, key, imgHeight, ImgWidth
        )
    }
}

// Simple data class, representing one message in local
data class FriendlyMessageDomain(
    val text: String? = null,
    val name: String? = null,
    val photoUrl: String? = null,
    val key: String? = null,
    val imgHeight: Int? = null,
    val ImgWidth: Int? = null
)


