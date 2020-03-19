package dev.foodie.retrofitdemo.models

import com.google.gson.annotations.SerializedName

data class Post (
    var userId: Int,

    var id: Int? = null,

    var title: String,

    @SerializedName("body")
    var text: String
) {
    override fun toString(): String {
        return "ID: ${ id }\nUser Id: ${ userId }\nTitle: ${ title }\nText: ${ text }\n"
    }
}