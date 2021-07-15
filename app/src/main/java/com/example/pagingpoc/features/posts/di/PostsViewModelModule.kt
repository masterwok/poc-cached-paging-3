package com.example.pagingpoc.features.posts.di

import androidx.lifecycle.ViewModel
import com.example.pagingpoc.di.annotations.ViewModelKey
import com.example.pagingpoc.features.posts.PostsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PostsViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(PostsViewModel::class)
    internal abstract fun bindViewModel(viewModel: PostsViewModel): ViewModel
}