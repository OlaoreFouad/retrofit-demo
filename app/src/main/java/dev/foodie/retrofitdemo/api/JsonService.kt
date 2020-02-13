package dev.foodie.retrofitdemo.api

import dev.foodie.retrofitdemo.models.Post
import retrofit2.Call
import retrofit2.http.GET

interface JsonService {

    @GET("posts")
    fun getPosts(): Call<List<Post>>

}