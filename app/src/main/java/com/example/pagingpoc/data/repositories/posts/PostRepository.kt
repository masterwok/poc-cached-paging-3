package com.example.pagingpoc.data.repositories.posts

import androidx.paging.Pager
import com.example.pagingpoc.data.models.Post

interface PostRepository {

    val postPager: Pager<Int, Post>

}