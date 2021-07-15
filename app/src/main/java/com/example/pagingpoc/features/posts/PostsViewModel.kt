package com.example.pagingpoc.features.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.pagingpoc.data.repositories.posts.PostRepository
import javax.inject.Inject

class PostsViewModel
@Inject constructor(
    postRepository: PostRepository
) : ViewModel() {

    val postFlow = postRepository
        .postPager
        .flow
        .cachedIn(viewModelScope)

}