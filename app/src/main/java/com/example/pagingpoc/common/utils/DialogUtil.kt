package com.example.pagingpoc.common.utils

import android.content.Context
import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.example.pagingpoc.R
import com.example.pagingpoc.common.exceptions.NoInternetConnectionException
import com.example.pagingpoc.data.models.EventFailure

fun presentMessageDialog(
    context: Context,
    @StringRes titleRes: Int? = null,
    @StringRes messageRes: Int? = null
) = MaterialDialog(context).show {
    cornerRadius(8f)
    negativeButton(R.string.ok) { dismiss() }
    titleRes?.let { title(res = titleRes) }
    messageRes?.let { message(res = messageRes) }
}

/**
 * Present a dialog when the user tries to make a network request while their device is not
 * connected to the internet.
 */
fun presentNotConnectedDialog(context: Context) {
    presentMessageDialog(
        context,
        titleRes = R.string.not_connected_dialog_title,
        messageRes = R.string.not_connected_dialog_message
    )
}

fun presentNetworkFailureDialog(
    context: Context,
    failure: EventFailure,
    okayAction: (() -> Unit)? = null,
    retryAction: (() -> Unit)? = null
) = when (failure.reason) {
    is NoInternetConnectionException -> presentNotConnectedDialog(context)
    else -> presentOopsDialog(
        context,
        R.string.oops_dialog_message_retry,
        okayAction,
        retryAction
    )
}

fun presentOopsDialog(
    context: Context,
    @StringRes message: Int,
    okayAction: (() -> Unit)? = null,
    retryAction: (() -> Unit)? = null
) {
    MaterialDialog(context).show {
        cornerRadius(8f)
        title(R.string.oops_dialog_title)
        cancelable(false)
        message(message)
        retryAction?.let {
            negativeButton(R.string.retry) {
                it()
            }
        }
        positiveButton(R.string.ok) {
            dismiss()
            okayAction?.invoke()
        }
    }
}