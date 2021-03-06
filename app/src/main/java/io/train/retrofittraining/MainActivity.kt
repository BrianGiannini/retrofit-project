package io.train.retrofittraining

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.train.retrofittraining.model.Comment
import io.train.retrofittraining.model.JsonPlaceHolderApi
import io.train.retrofittraining.model.Post
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    var jsonPlaceHolderApi: JsonPlaceHolderApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gson: Gson = GsonBuilder().serializeNulls().create()

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor {
                val newRequest = it.request().newBuilder()
                    .header("Interceptro-Header", "xyz")
                    .build()

                return@Interceptor it.proceed(newRequest)
            })
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofitBuilder: Retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

        jsonPlaceHolderApi = retrofitBuilder.create(JsonPlaceHolderApi::class.java)

        getPosts()
//        getComments()
//        createPost()
//        updatePost()
//        deletePost()
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
                    return
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
        val call = jsonPlaceHolderApi?.getComments("posts/3/comments")

        call?.enqueue(object : Callback<List<Comment>> {
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

    private fun createPost() {
        val post = Post(23, "New Title", "New Text")
        val fields: Map<String, String> = mapOf("userId" to "25", "title" to "New Title")

        val call: Call<Post> = jsonPlaceHolderApi!!.createPost(fields)

        call.enqueue(object: Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                textViewResult.append(t.message)
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if(!response.isSuccessful) {
                    textViewResult.text = "Code ${response.code()}"
                    return
                }

                val postResponse: Post = response.body()!!

                var content = ""
                content += "Code " + response.code() + "\n"
                content += "ID: ${postResponse.id} \n"
                content += "User ID: ${postResponse.userId} \n"
                content += "Title: ${postResponse.title} \n"
                content += "Text: ${postResponse.text} \n\n"

                textViewResult.append(content)
            }
        })
    }

    private fun updatePost() {
        val post = Post(12, null, "New Text")
        val headers: Map<String, String> = mapOf("Map-Header1" to "def", "Map-Header2" to "ghi")

        val call: Call<Post> = jsonPlaceHolderApi!!.patchPost(headers, 5, post)

        call.enqueue(object: Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                textViewResult.append(t.message)
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                val postResponse: Post = response.body()!!

                var content = ""
                content += "Code " + response.code() + "\n"
                content += "ID: ${postResponse.id} \n"
                content += "User ID: ${postResponse.userId} \n"
                content += "Title: ${postResponse.title} \n"
                content += "Text: ${postResponse.text} \n\n"

                textViewResult.append(content)            }
        })

    }

    private fun deletePost() {
        val call: Call<Unit> = jsonPlaceHolderApi!!.deletePost(5)

        call.enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                textViewResult.append(t.message)
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                textViewResult.append("Code ${response.code()}" )

            }
        })
    }
}
