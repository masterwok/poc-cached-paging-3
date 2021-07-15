package com.example.pagingpoc.di

import android.content.Context
import com.example.pagingpoc.data.di.NetworkModule
import com.example.pagingpoc.data.di.RepositoryModule
import com.example.pagingpoc.di.modules.SubcomponentModules
import com.example.pagingpoc.features.posts.di.PostsComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        RepositoryModule::class,
        SubcomponentModules::class
    ]
)
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance appContext: Context): ApplicationComponent
    }

    fun portfolioComponent(): PostsComponent.Factory

}