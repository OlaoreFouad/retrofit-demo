package dev.foodie.retrofitdemo.api

import dev.foodie.retrofitdemo.models.Comment
import dev.foodie.retrofitdemo.models.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface JsonService {

    @GET("posts")
    fun getPosts(): Call<List<Post>>

    @GET("posts/{id}/comments")
    fun getCommentsForUser(@Path("id") userId: Int): Call<List<Comment>>

}