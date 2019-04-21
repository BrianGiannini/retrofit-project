package io.train.retrofittraining.model

import com.google.gson.annotations.SerializedName

data class Comment (

    internal val postId: Int,
    internal val id: Int,
    internal val name: String,
    internal val email: String,
    @SerializedName("body")
    internal val text: String

    )