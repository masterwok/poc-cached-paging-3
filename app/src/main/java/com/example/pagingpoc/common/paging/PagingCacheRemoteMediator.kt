package com.example.pagingpoc.common.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator


@OptIn(ExperimentalPagingApi::class)
class PagingCacheRemoteMediator<Key : Any, Item : Any, ItemId : Any>(
    private val pageFetcher: PageFetcher<Key, Item>,
    private val pagingCache: PagingCache<Key, Item, ItemId>,
    private val startingPageKey: Key
) : RemoteMediator<Key, Item>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Key, Item>
    ): MediatorResult {
        val key: Key = when (loadType) {
            LoadType.REFRESH -> startingPageKey
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> state
                .lastItemOrNull()
                ?.let { pagingCache.getPageKeyForItem(it) }
                ?.nextPageKey
                ?: return MediatorResult.Success(endOfPaginationReached = true)
        }

        return try {
            val page = pageFetcher.fetchPage(key)

            pagingCache.Transaction {
                if (loadType == LoadType.REFRESH) {
                    clear()
                }

                if (page.items.isNotEmpty()) {
                    insert(page.items, page.key)
                }
            }

            MediatorResult.Success(endOfPaginationReached = page.items.isEmpty())
        } catch (error: Throwable) {
            Log.d(TAG, "Exception!!")
            MediatorResult.Error(error)
        }.also { result: MediatorResult ->
            Log.d(TAG, "--------")
            Log.d(TAG, "Mediator request load type: $loadType")

            if (result is MediatorResult.Success) {
                Log.d(
                    TAG,
                    "Successful Mediator result, endOfPaginationReached=${result.endOfPaginationReached}"
                )
            } else if (result is MediatorResult.Error) {
                Log.d(TAG, "Mediator failed with: ${result.throwable}")
            }

        }
    }

    companion object {
        private const val TAG = "REMOTE_MEDIATOR"
    }


}