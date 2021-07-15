package com.example.pagingpoc.data.models

/**
 * Represents the event state of an operation.
 */
sealed class EventState

/**
 * Indicates that the event is currently pending (performing some operation).
 */
object EventPending : EventState()

/**
 * Indicates that a pending event completed successfully.
 */
object EventSuccess : EventState()

/**
 * Indicates that a pending event failed due to the [reason].
 */
data class EventFailure(val reason: Throwable) : EventState()
