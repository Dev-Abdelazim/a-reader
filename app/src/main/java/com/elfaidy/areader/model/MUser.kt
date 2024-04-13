package com.elfaidy.areader.model

import com.google.firebase.firestore.PropertyName

data class MUser(
    var id: String?,
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String,
    @get:PropertyName("first_name")
    @set:PropertyName("first_name")
    var firstName: String,
    @get:PropertyName("last_name")
    @set:PropertyName("last_name")
    var lastName: String,
    var email: String,
    @get:PropertyName("avatar_url")
    @set:PropertyName("avatar_url")
    var avatarUrl: String,
    var quote: String,
    var profession: String
){
    fun toMap(): MutableMap<String, String?>{
        return mutableMapOf(
            "id" to this.id,
            "user_id" to this.userId,
            "first_name" to this.firstName,
            "last_name" to this.lastName,
            "email" to this.email,
            "avatar_url" to this.avatarUrl,
            "quote" to this.quote,
            "profession" to this.profession,
        )
    }
}
