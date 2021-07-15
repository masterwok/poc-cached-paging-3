package com.example.pagingpoc.data.di

import android.content.Context
import com.example.pagingpoc.common.interceptors.InternetConnectionInterceptor
import com.example.pagingpoc.common.moshi.adapters.UriAdapter
import com.example.pagingpoc.data.clients.reqres.JsonPlaceholderClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(UriAdapter.FACTORY)
        .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(appContext: Context): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(InternetConnectionInterceptor(appContext))
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    @Singleton
    @Provides
    fun provideJsonPlaceholderClient(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit
        .Builder()
        .baseUrl("https://jsonplaceholder.typicode.com")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideReqresClient(retrofit: Retrofit): JsonPlaceholderClient = retrofit
        .create(JsonPlaceholderClient::class.java)

}