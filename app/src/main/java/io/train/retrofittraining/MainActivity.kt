package io.train.retrofittraining

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.train.retrofittraining.model.JsonPlaceHolderApi
import io.train.retrofittraining.model.Post
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofitBuilder: Retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val jsonPlaceHolderApi: JsonPlaceHolderApi = retrofitBuilder.create(JsonPlaceHolderApi::class.java)

        val callApi: Call<List<Post>> = jsonPlaceHolderApi.getPosts()

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
                    content += "Test: ${post.text} \n\n"

                    textViewResult.append(content)

                }
            }

        })

    }


}
