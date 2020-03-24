package dev.foodie.retrofitdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.GsonBuilder
import dev.foodie.retrofitdemo.api.JsonService
import dev.foodie.retrofitdemo.models.Comment
import dev.foodie.retrofitdemo.models.Post
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var jsonService: JsonService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * A logging interceptor is a program that intercepts requests sent by the client and responses, and logs them for developers to see.
         * How To?
         * 1. Get the okhttp logging interceptor dependency
         * 2. Initialize an object pointing to the logging interceptor
         * 3. Create an okttp client, and add the logging interceptor to it to enable logging.
         * 4. Register the client to the retrofit instance to add it. Retrofit uses Ok-http in the background.
         * */

        val gson = GsonBuilder().serializeNulls().create()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    val incomingRequest = chain.request()

                    val outgoingRequest = incomingRequest.newBuilder()
                        .header("LoggingInterceptor", "LoggingInterceptorValue")
                        .build()

                    return chain.proceed(outgoingRequest)
                }
            })
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

        jsonService = retrofit.create(JsonService::class.java)

        //getPosts()
        //getCommentsForUser(3)
//        getPost()
        //createPost()
        putPost()
        //patchPost("Fooo", "Barrrr")
        deletePost(7)
    }

    private fun getPosts() {
        val posts = jsonService.getPosts()

        posts.enqueue(object : Callback<List<Post>> {
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                text_view_result.text = t.message
            }

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    text_view_result.text = "Status Code: ${ response.code() }"
                    return
                }

                val psts = response.body()
                // Looping through each of the posts
                psts?.forEach {
                    text_view_result.append(
                        "Id: ${ it.id }\nUser ID: ${ it.userId }\nTitle: ${ it.title }\nContent: ${ it.text }"
                    )
                    text_view_result.append("\n")
                }
            }
        })
    }

    private fun getCommentsForUser(userId: Int) {

        val comments = jsonService.getCommentsForUser(userId)

        comments.enqueue(object : Callback<List<Comment>> {
            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                text_view_result.text = t.message
            }

            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {

                if (!response.isSuccessful) {
                    text_view_result.text = "Status Code: ${ response.code() }"
                    return
                }

                val comments = response.body()!!
                comments.forEach {
                    text_view_result.append(
                        "Id: ${ it.id }\nPost Id: ${ it.postId }\nName: ${ it.name }\nEmail: ${ it.email }\nContent: ${ it.content }\n"
                    )

                    text_view_result.append("\n")
                }

            }
        })

    }

    private fun getPost() {

        jsonService.getPost(1).enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                text_view_result.text = t.message
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    text_view_result.text = "Code: ${ response.code() }"
                    return
                }

                val post: Post = response.body()!!
                text_view_result.text = post.toString()
            }
        })

    }

    private fun createPost() {
        val post = Post(title = "Fouad's Title", text = "This is some very random text!", userId = 45)
        jsonService.createPost(mapOf("id" to "22", "userId" to "11", "title" to post.title, "body" to post.text)).enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                text_view_result.text = "Message: ${ t.message }"
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    text_view_result.text = "Code: ${ response.code() }"
                }

                val p = response.body()
                text_view_result.text = p.toString()
            }
        })
    }

    private fun putPost() {
        val post = Post(3, 5, "This is a new title for id 5", "Text to follow through!")
        jsonService.putPost("token being passed in!",3, post).enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                text_view_result.text = t.message
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    text_view_result.text = "Code: ${ response.code() }"
                    return
                }

                text_view_result.text = response.body().toString()

            }
        })
    }

    private fun patchPost(title: String, text: String, userId: Int = 5) {
        jsonService.patchPost(9, mapOf(
            "title" to title, "body" to text, "userId" to "$userId"
        )).enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                text_view_result.text = t.message
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    text_view_result.text = "Code: ${ response.code() }"
                    return
                }

                text_view_result.text = response.body().toString()

            }
        })
    }

    private fun deletePost(id: Int) {
        jsonService.deletePost(id).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                text_view_result.text = t.message
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                text_view_result.text = "Code: ${ response.code() }"
            }
        })
    }

}
