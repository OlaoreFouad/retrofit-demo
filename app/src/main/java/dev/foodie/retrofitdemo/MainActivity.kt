package dev.foodie.retrofitdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.foodie.retrofitdemo.api.JsonService
import dev.foodie.retrofitdemo.models.Comment
import dev.foodie.retrofitdemo.models.Post
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var jsonService: JsonService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        jsonService = retrofit.create(JsonService::class.java)

        //getPosts()
        getCommentsForUser(3)
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

}
