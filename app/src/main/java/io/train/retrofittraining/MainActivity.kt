package io.train.retrofittraining

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.train.retrofittraining.model.Comment
import io.train.retrofittraining.model.JsonPlaceHolderApi
import io.train.retrofittraining.model.Post
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private var jsonPlaceHolderApi: JsonPlaceHolderApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofitBuilder: Retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        jsonPlaceHolderApi = retrofitBuilder.create(JsonPlaceHolderApi::class.java)

        //getPosts()
        getComments()

    }

    private fun getPosts() {
        val parameters: Map<String, String> = mapOf("userId" to "1", "_sort" to "id", "_order" to "desc")

        val callApi: Call<List<Post>> = jsonPlaceHolderApi!!.getPosts(parameters)

        callApi.enqueue(object: Callback<List<Post>> {
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                textViewResult.text = t.message
            }

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if(!response.isSuccessful) {
                    textViewResult.text = "Code ${response.code()}"
                }

                val posts = response.body()

                for (post: Post in posts!!) {
                    var content = ""
                    content += "ID: ${post.id} \n"
                    content += "User ID: ${post.userId} \n"
                    content += "Title: ${post.title} \n"
                    content += "Text: ${post.text} \n\n"

                    textViewResult.append(content)

                }
            }

        })
    }
    
    fun getComments() {
        val callapi = jsonPlaceHolderApi?.getComments("posts/3/comments")

        callapi?.enqueue(object : Callback<List<Comment>> {
            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                textViewResult.text = t.message
            }

            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if(!response.isSuccessful) {
                    textViewResult.text = "Code ${response.code()}"
                    return
                }

                val comments = response.body()

                for (comment: Comment in comments!!) {
                    var content = ""
                    content += "ID: ${comment.id} \n"
                    content += "Post ID: ${comment.postId} \n"
                    content += "Name: ${comment.name} \n"
                    content += "Email: ${comment.email} \n"
                    content += "Text: ${comment.text} \n\n"

                    textViewResult.append(content)
                }
            }

        })
    }


}
