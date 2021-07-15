package com.example.pagingpoc.data.models

import com.example.pagingpoc.data.clients.jsonplaceholder.dtos.PostDto

data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
) {
    companion object
}

fun Post.Companion.from(postDto: PostDto): Post = with(postDto) {
    Post(
        id = id,
        userId = userId,
        title = title,
        body = body
    )
}