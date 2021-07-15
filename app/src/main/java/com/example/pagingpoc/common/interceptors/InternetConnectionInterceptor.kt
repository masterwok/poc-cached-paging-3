package com.example.pagingpoc.common.interceptors

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.pagingpoc.common.exceptions.NoInternetConnectionException
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

/**
 * An [Interceptor] that checks to see if the device has an internet connection
 * before a request is made.
 */
internal class InternetConnectionInterceptor(context: Context) : Interceptor {

    private val internetCapableNetworks = Collections.synchronizedSet(mutableSetOf<Network>())

    private val isConnected get() = internetCapableNetworks.isNotEmpty()

    private val connectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onLost(network: Network) {
            internetCapableNetworks.remove(network)
        }

        override fun onAvailable(network: Network) {
            val isInternetCapableNetwork = isInternetCapableNetwork(network)

            if (isInternetCapableNetwork) {
                internetCapableNetworks.add(network)
            }
        }
    }

    init {
        readInternetCapableNetworks()
        registerNetworkCallback()
    }

    private fun registerNetworkCallback() {
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun readInternetCapableNetworks() = connectivityManager
        .allNetworks
        .filter(::isInternetCapableNetwork)
        .forEach {
            internetCapableNetworks.add(it)
        }

    private fun isInternetCapableNetwork(network: Network) = connectivityManager
        .getNetworkCapabilities(network)
        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        ?: false


    override fun intercept(chain: Interceptor.Chain): Response {
        if (isConnected) {
            return chain.proceed(chain.request())
        }

        throw NoInternetConnectionException()
    }

}