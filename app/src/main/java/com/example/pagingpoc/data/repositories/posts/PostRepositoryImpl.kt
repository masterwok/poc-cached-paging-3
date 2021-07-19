package com.example.pagingpoc.data.repositories.posts

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.pagingpoc.common.paging.PageFetcher
import com.example.pagingpoc.common.paging.PagingCache
import com.example.pagingpoc.common.paging.PagingCacheRemoteMediator
import com.example.pagingpoc.data.models.Post
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalPagingApi::class)
class PostRepositoryImpl @Inject constructor(
    postPagingCache: PagingCache<Int, Post, Int>,
    userPageFetcher: PageFetcher<Int, Post>,
    @Named("PAGE_SIZE") private val pageSize: Int,
    @Named("STARTING_PAGE_KEY") private val startingPageKey: Int
) : PostRepository {

    override val postPager = Pager(
        config = PagingConfig(pageSize),
        initialKey = startingPageKey,
        remoteMediator = PagingCacheRemoteMediator(
            pagingCache = postPagingCache,
            startingPageKey = startingPageKey,
            pageFetcher = userPageFetcher
        ),
        pagingSourceFactory = postPagingCache.pagingSourceFactory
    )
}