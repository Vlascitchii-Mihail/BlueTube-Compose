package com.vlascitchii.presentation_common.network_observer

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class NetworkConnectivityObserver (context: Context) : NetworkConnectivityAbstraction {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<ConnectivityStatus> = callbackFlow {
        Log.d("Snack", "observe() called")
        val callback = createNetworkCallback { connectivityStatus -> trySend(connectivityStatus) }

        connectivityManager.registerDefaultNetworkCallback(callback)
        trySend(getCurrentConnectivityState())

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()

    private fun createNetworkCallback(callback: (ConnectivityStatus) -> Unit)
            : ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                callback.invoke(ConnectivityStatus.Available)
            }

            override fun onLost(network: Network) {
                callback.invoke(ConnectivityStatus.Lost)
            }
        }
    }

    private fun getCurrentConnectivityState(): ConnectivityStatus {
        val activeNetwork = connectivityManager.activeNetwork
        val isConnected = connectivityManager.getNetworkCapabilities(activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false

        return if (isConnected) ConnectivityStatus.Available else ConnectivityStatus.Lost
    }
}
