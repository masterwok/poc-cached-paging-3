package com.example.pagingpoc.common.paging

import com.example.pagingpoc.features.posts.models.PageKey

data class Page<Key : Any, Item : Any>(
    val key: PageKey<Key>,
    val items: List<Item>
)