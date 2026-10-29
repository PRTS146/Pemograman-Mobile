package com.example.pert9pemmob.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Data class representing a Post object from JSONPlaceholder API.
 */
data class Post(
    @SerializedName("userId")
    val userId: Int,

    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("body")
    val body: String
) : Serializable
