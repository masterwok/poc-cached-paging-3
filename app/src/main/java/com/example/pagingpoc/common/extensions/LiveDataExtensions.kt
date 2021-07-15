package com.example.pagingpoc.common.extensions

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.pagingpoc.data.models.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Track an operation through [Event] states.
 */
suspend fun <T> MutableLiveData<Event<EventState>>.trackEvent(
    block: suspend () -> T
): T? = coroutineScope {
    postValue(Event(EventPending))

    var networkFailure: Event<EventFailure>? = null

    val deferred: Deferred<T?> = async {
        try {
            block()
        } catch (ex: Exception) {
            networkFailure = Event(EventFailure(ex))
            Log.e("TRACK_REQUEST", "Async call failed", ex)
            null
        }
    }

    deferred.await().also {
        postValue(networkFailure ?: Event(EventSuccess))
    }
}

/**
 * Subscribe the [lifecycleOwner] to a one-time [Event] of type, [T]. If an [Event] has
 * already be consumed, then the [Event] won't be emitted again. This is useful in preventing
 * a consumer from consuming the same data from a single [Event] more than once.
 */
internal inline fun <T : Any> LiveData<Event<T>>.observeUnconsumed(
    lifecycleOwner: LifecycleOwner,
    crossinline onEmit: (T) -> Unit
): Observer<Event<T>> {
    val observer = Observer<Event<T>> { event ->
        if (!event.isConsumed) {
            onEmit(checkNotNull(event.consume()))
        }
    }

    observe(lifecycleOwner, observer)
    return observer
}