package com.example.pagingpoc.data.caches

import com.example.pagingpoc.common.paging.Page
import com.example.pagingpoc.common.paging.PageFetcher
import com.example.pagingpoc.data.clients.reqres.JsonPlaceholderClient
import com.example.pagingpoc.data.models.Post
import com.example.pagingpoc.data.models.from
import com.example.pagingpoc.features.posts.models.PageKey

class PostPageFetcher(
    private val jsonPlaceholderClient: JsonPlaceholderClient,
    private val pageSize: Int,
    private val startingPageIndex: Int
) : PageFetcher<Int, Post> {

    override suspend fun fetchPage(key: Int): Page<Int, Post> {
        val response = jsonPlaceholderClient.getPosts(key, pageSize)

        return Page(
            key = PageKey(
                value = key,
                previousPageKey = if (key == startingPageIndex) null else key - 1,
                nextPageKey = if (response.isEmpty()) null else key + 1
            ),
            items = response.map(Post.Companion::from)
        )


    }
}