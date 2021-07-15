package com.example.pagingpoc.common.paging

import androidx.paging.PagingSource

class InvalidatingPagingSourceFactory<Key : Any, Value : Any>(
    private val pagingSourceFactory: () -> PagingSource<Key, Value>
) : () -> PagingSource<Key, Value> {
    private val pagingSources = mutableListOf<PagingSource<Key, Value>>()

    override fun invoke(): PagingSource<Key, Value> {
        return pagingSourceFactory().also { pagingSources.add(it) }
    }

    fun invalidate() {
        for (pagingSource in pagingSources.toList()) {
            if (!pagingSource.invalid) {
                pagingSource.invalidate()
            }
        }

        pagingSources.removeAll { it.invalid }
    }
}
