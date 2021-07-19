package com.example.pagingpoc.common.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pagingpoc.features.posts.models.PageKey

class PagingCachePagingSource<Key : Any, Item : Any, ItemId : Any>(
    private val itemIdResolver: ItemIdResolver<Item, ItemId>,
    private val itemIdToPageKeyMap: Map<ItemId, PageKey<Key>>,
    private val cache: Map<Key, List<Item>>,
    private val pageKeysIndex: Map<Key, PageKey<Key>>,
    private val startingKey: Key
) : PagingSource<Key, Item>() {

    private val emptyLoadResult = LoadResult.Page<Key, Item>(emptyList(), null, null, 0, 0)

    override fun getRefreshKey(
        state: PagingState<Key, Item>
    ): Key? {
        return state.anchorPosition?.let { anchorPosition ->
            val closestItem = state.closestItemToPosition(anchorPosition) ?: return@let startingKey
            val closestItemId = itemIdResolver.getId(closestItem)

            itemIdToPageKeyMap[closestItemId]?.value ?: startingKey
        }
    }

    override suspend fun load(params: LoadParams<Key>): LoadResult<Key, Item> = try {
        val pageKey = pageKeysIndex[params.key]

        if (pageKey == null || !cache.containsKey(pageKey.value)) {
            emptyLoadResult
        } else {
            LoadResult.Page(
                data = cache.getValue(pageKey.value),
                pageKeysIndex[pageKey.previousPageKey]?.value,
                pageKeysIndex[pageKey.nextPageKey]?.value
            )
        }
    } catch (error: Throwable) {
        LoadResult.Error(error)
    }.also { result ->
        Log.d(TAG, "--------")
        Log.d(TAG, "Paging source params key: ${params.key}")
        Log.d(TAG, "Paging source params loadSize: ${params.loadSize}")

        if (result is LoadResult.Page) {
            Log.d(TAG, "Paging source page prevKey: ${result.prevKey}")
            Log.d(TAG, "Paging source page nextKey: ${result.nextKey}")
        } else if (result is LoadResult.Error) {
            Log.d(TAG, "Paging source error: ${result.throwable}")
        }
    }

    private companion object {
        const val TAG = "PAGING_SOURCE"
    }
}