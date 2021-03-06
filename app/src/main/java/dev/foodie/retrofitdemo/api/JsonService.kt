package dev.foodie.retrofitdemo.api

import dev.foodie.retrofitdemo.models.Comment
import dev.foodie.retrofitdemo.models.Post
import retrofit2.Call
import retrofit2.http.*

interface JsonService {

    @GET("posts")
    fun getPosts(): Call<List<Post>>

    @GET("posts/{id}/comments")
    fun getCommentsForUser(@Path("id") userId: Int): Call<List<Comment>>

    @GET("posts/{id}")
    fun getPost(@Path("id") id: Int): Call<Post>

    @POST("posts")
    fun createPost(@Body post: Post): Call<Post>

    @FormUrlEncoded
    @POST("posts")
    fun createPost(
        @Field("id") id: Int,
        @Field("userId") userId: Int,
        @Field("title") title: String
    ): Call<Post>

    @FormUrlEncoded
    @POST("posts")
    fun createPost(
        @FieldMap fieldMap: Map<String, String>
    ): Call<Post>

    @Headers(
        "Fake-header1: fake_value",
        "Fake-header2: fake_value"
    )
    @PUT("posts/{id}")
    fun putPost(@Header("Authorization") token: String,  @Path("id") id: Int, @Body post: Post): Call<Post>

    @FormUrlEncoded
    @PATCH("posts/{id}")
    fun patchPost(@Path("id") id: Int, @FieldMap map: Map<String, String>): Call<Post>

    @DELETE("posts/{id}")
    fun deletePost(
        @Path("id") id: Int
    ): Call<Void>

}