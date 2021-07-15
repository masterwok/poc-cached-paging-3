package com.example.pagingpoc.data.clients.reqres

import com.example.pagingpoc.data.clients.reqres.dtos.PostDto
import retrofit2.http.GET
import retrofit2.http.Query

interface JsonPlaceholderClient {

    @GET("posts")
    suspend fun getPosts(
        @Query("_page") pageIndex: Int,
        @Query("_limit") pageSize: Int
    ): List<PostDto>

}