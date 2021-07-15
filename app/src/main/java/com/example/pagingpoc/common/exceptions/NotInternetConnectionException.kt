package com.example.pagingpoc.common.exceptions

import java.io.IOException

/**
 * Used to specify that the device isn't connected to the internet.
 */
internal class NoInternetConnectionException : IOException("No network available")