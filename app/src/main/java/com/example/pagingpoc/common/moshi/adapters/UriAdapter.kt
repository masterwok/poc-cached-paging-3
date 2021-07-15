package com.example.pagingpoc.common.moshi.adapters

import android.net.Uri
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import okio.IOException

class UriAdapter : JsonAdapter<Uri>() {

    companion object {
        val FACTORY = Factory { type, _, _ ->
            return@Factory if (type === Uri::class.java) {
                UriAdapter()
            } else {
                null
            }
        }
    }

    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): Uri {
        return Uri.parse(reader.nextString())
    }

    @Throws(IOException::class)
    override fun toJson(
        writer: JsonWriter,
        value: Uri?
    ) {
        writer.value(value?.toString())
    }

    override fun toString(): String {
        return "JsonAdapter(Uri)"
    }
}