package com.example.pagingpoc.common.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.pagingpoc.features.posts.models.PageKey


@OptIn(ExperimentalPagingApi::class)
class PagingCacheRemoteMediator<Key : Any, Item : Any, ItemId : Any>(
    private val pageFetcher: PageFetcher<Key, Item>,
    private val itemIdResolver: ItemIdResolver<Item, ItemId>,
    private val pagingCache: PagingCache<Key, Item, ItemId>,
    private val startingPageKey: Key
) : RemoteMediator<Key, Item>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Key, Item>
    ): MediatorResult {
        val key = when (loadType) {
            LoadType.REFRESH -> {
                val pageKey = getRemoteKeyClosestToCurrentPosition(state)
                pageKey?.nextPageKey ?: startingPageKey
            }
            LoadType.PREPEND -> {
                val pageKey = getRemoteKeyForFirstItem(state)
                val prevKey = pageKey?.previousPageKey
                    ?: return MediatorResult.Success(endOfPaginationReached = pageKey != null)

                prevKey
            }
            LoadType.APPEND -> {
                val pageKey = getRemoteKeyForLastItem(state)
                val nextKey = pageKey?.nextPageKey
                    ?: return MediatorResult.Success(endOfPaginationReached = pageKey != null)
                nextKey
            }
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
//            Log.d(TAG, "Mediator request state: $state")
//            Log.d(TAG, "Mediator request result: $result")

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

    private fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Key, Item>): PageKey<Key>? {
        val anchorPosition = state.anchorPosition ?: return null
        val closetItem = state.closestItemToPosition(anchorPosition) ?: return null
        val itemId = itemIdResolver.getId(closetItem)

        return pagingCache.getPageKeyForItemId(itemId)
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Key, Item>): PageKey<Key>? {
        val firstPage = state.pages.firstOrNull { it.data.isNotEmpty() } ?: return null
        val firstItem = firstPage.data.firstOrNull() ?: return null
        val itemId = itemIdResolver.getId(firstItem)

        return pagingCache.getPageKeyForItemId(itemId)
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Key, Item>): PageKey<Key>? {
        val lastPage = state.pages.lastOrNull() { it.data.isNotEmpty() } ?: return null
        val lastItem = lastPage.data.lastOrNull() ?: return null
        val itemId = itemIdResolver.getId(lastItem)

        return pagingCache.getPageKeyForItemId(itemId)
    }

    companion object {
        private const val TAG = "REMOTE_MEDIATOR"
    }


}