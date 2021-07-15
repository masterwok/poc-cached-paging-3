package com.example.pagingpoc.common.paging

interface PageFetcher<Key : Any, Item : Any> {
    suspend fun fetchPage(key: Key): Page<Key, Item>
}