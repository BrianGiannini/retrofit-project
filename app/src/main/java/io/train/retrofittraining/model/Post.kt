package io.train.retrofittraining.model

import com.google.gson.annotations.SerializedName

data class Post (

    internal val userId: Int,
    internal val id: Int,
    internal val title: String,
    @SerializedName("body")
    internal val text: String

    )