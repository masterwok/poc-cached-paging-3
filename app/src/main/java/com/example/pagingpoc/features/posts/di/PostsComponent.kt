package com.example.pagingpoc.features.posts.di

import com.example.pagingpoc.di.modules.ViewModelFactoryModule
import com.example.pagingpoc.features.posts.PostsFragment
import dagger.Subcomponent

@PostsScope
@Subcomponent(
    modules = [
        ViewModelFactoryModule::class,
        PostsViewModelModule::class
    ]
)
interface PostsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): PostsComponent
    }

    fun inject(fragment: PostsFragment)

}