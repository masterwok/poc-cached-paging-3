package com.example.pagingpoc.di.modules

import androidx.lifecycle.ViewModelProvider
import com.example.pagingpoc.di.factories.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module
internal abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(
        viewModelFactory: DaggerViewModelFactory
    ): ViewModelProvider.Factory

}