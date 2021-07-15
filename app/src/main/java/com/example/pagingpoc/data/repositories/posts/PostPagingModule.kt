package com.example.pagingpoc.data.repositories.posts

import com.example.pagingpoc.common.paging.PageFetcher
import com.example.pagingpoc.common.paging.PagingCache
import com.example.pagingpoc.data.caches.PostPageFetcher
import com.example.pagingpoc.data.clients.jsonplaceholder.JsonPlaceholderClient
import com.example.pagingpoc.data.models.Post
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [PostPagingModule.Declarations::class])
class PostPagingModule {

    @Named("PAGE_SIZE")
    @Provides
    fun providePageSize(): Int = PAGE_SIZE

    @Named("STARTING_PAGE_KEY")
    @Provides
    fun provideStartingPageKey(): Int = STARTING_PAGE_INDEX

    @Provides
    fun providesPagingCache(
        @Named("STARTING_PAGE_KEY") startingPageKey: Int
    ): PagingCache<Int, Post, Int> = PagingCache(
        Post::id,
        startingPageKey
    )

    @Provides
    fun provideUserPageFetcher(
        jsonPlaceholderClient: JsonPlaceholderClient
    ): PageFetcher<Int, Post> = PostPageFetcher(
        jsonPlaceholderClient = jsonPlaceholderClient,
        PAGE_SIZE,
        STARTING_PAGE_INDEX
    )

    @Module
    interface Declarations

    private companion object {
        const val PAGE_SIZE = 10
        const val STARTING_PAGE_INDEX = 1
    }
}