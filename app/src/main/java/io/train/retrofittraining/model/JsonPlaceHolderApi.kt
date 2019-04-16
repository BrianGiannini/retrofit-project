package io.train.retrofittraining.model

import retrofit2.Call
import retrofit2.http.GET

interface JsonPlaceHolderApi {

    @GET("posts")
    fun getPosts(): Call<List<Post>>

}