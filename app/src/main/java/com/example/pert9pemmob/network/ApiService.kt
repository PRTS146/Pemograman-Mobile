package com.example.pert9pemmob.network

import com.example.pert9pemmob.data.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit ApiService interface for JSONPlaceholder API endpoints.
 */
interface ApiService {

    /**
     * Endpoint 1: Fetch all posts
     * GET /posts
     */
    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>

    /**
     * Endpoint 2: Fetch a single post by ID
     * GET /posts/{id}
     */
    @GET("posts/{id}")
    suspend fun getPostById(
        @Path("id") id: Int
    ): Response<Post>
}
