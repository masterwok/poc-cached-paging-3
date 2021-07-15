package com.example.pagingpoc.features.posts.models

data class PageKey<KeyType : Any>(
    val value: KeyType,
    val previousPageKey: KeyType? = null,
    val nextPageKey: KeyType? = null
)