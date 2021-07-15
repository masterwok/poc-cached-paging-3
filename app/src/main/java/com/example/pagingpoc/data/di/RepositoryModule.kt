package com.example.pagingpoc.data.di

import com.example.pagingpoc.data.repositories.posts.PostPagingModule
import com.example.pagingpoc.data.repositories.posts.PostRepository
import com.example.pagingpoc.data.repositories.posts.PostRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(
    includes = [PostPagingModule::class]
)
abstract class RepositoryModule {

    @Singleton
    @Binds
    internal abstract fun bindUserRepository(repository: PostRepositoryImpl): PostRepository


}