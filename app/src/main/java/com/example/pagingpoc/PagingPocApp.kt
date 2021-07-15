package com.example.pagingpoc

import android.app.Application
import com.example.pagingpoc.di.ApplicationComponent
import com.example.pagingpoc.di.DaggerApplicationComponent

class PagingPocApp : Application() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerApplicationComponent
            .factory()
            .create(this.applicationContext)
    }


}