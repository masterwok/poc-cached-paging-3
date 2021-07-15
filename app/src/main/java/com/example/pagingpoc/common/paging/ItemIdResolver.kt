package com.example.pagingpoc.common.paging

fun interface ItemIdResolver<Item : Any, ItemId : Any> {

    fun getId(item: Item): ItemId

}