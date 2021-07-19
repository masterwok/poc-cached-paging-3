package com.example.pagingpoc.common.paging

import com.example.pagingpoc.features.posts.models.PageKey
import java.util.concurrent.atomic.AtomicBoolean

class PagingCache<Key : Any, Item : Any, ItemId : Any>(
    private val itemIdResolver: ItemIdResolver<Item, ItemId>,
    private val startingKey: Key
) {
    val pagingSourceFactory = InvalidatingPagingSourceFactory(::createPagingSource)

    private val cache = HashMap<Key, LinkedHashMap<ItemId, Item>>()
    private val itemIdToPageKeyMap = HashMap<ItemId, PageKey<Key>>()
    private val keyToPageKeyMap = HashMap<Key, PageKey<Key>>()

    fun getPageKeyForItemId(itemId: ItemId): PageKey<Key>? = itemIdToPageKeyMap[itemId]

    fun getPageKeyForItem(
        item: Item
    ): PageKey<Key>? = getPageKeyForItemId(itemIdResolver.getId(item))

    private fun createPagingSource() = PagingCachePagingSource(
        itemIdResolver,
        itemIdToPageKeyMap.toMap(),
        cache.map { (key, value) -> key to value.values.toList() }.toMap(),
        keyToPageKeyMap.toMap(),
        startingKey
    )

    private fun getPage(pageKey: Key): HashMap<ItemId, Item> = cache
        .getOrPut(pageKey) { linkedMapOf() }

    inner class Transaction constructor(block: Transaction.() -> Unit) {
        private val pendingInvalidation = AtomicBoolean(false)

        init {
            block(this)
            if (pendingInvalidation.compareAndSet(true, false)) {
                pagingSourceFactory.invalidate()
            }
        }

        fun insert(item: Item, pageKey: PageKey<Key>) {
            val page = getPage(pageKey.value)
            val itemId = itemIdResolver.getId(item)
            page[itemId] = item
            itemIdToPageKeyMap[itemId] = pageKey
            keyToPageKeyMap[pageKey.value] = pageKey
            pendingInvalidation.set(true)
        }

        fun insert(items: List<Item>, pageKey: PageKey<Key>) =
            items.forEach { item -> insert(item, pageKey) }

        fun clear() {
            cache.clear()
            itemIdToPageKeyMap.clear()
            keyToPageKeyMap.clear()
            pendingInvalidation.set(true)
        }
    }

}