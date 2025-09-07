package com.vlascitchii.presentation_common.network_observer

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class NetworkConnectivityObserver(
    var systemConnectivityManager: ConnectivityManager
) : NetworkConnectivityAbstraction {

    override fun observe(): Flow<NetworkConnectivityStatus> = callbackFlow {
        val callback = createNetworkCallback { connectivityStatus -> trySend(connectivityStatus) }

        systemConnectivityManager.registerDefaultNetworkCallback(callback)
        trySend(getCurrentConnectivityState())

        awaitClose { systemConnectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()

    private fun createNetworkCallback(callback: (NetworkConnectivityStatus) -> Unit)
            : ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                callback.invoke(NetworkConnectivityStatus.Available)
            }

            override fun onLost(network: Network) {
                callback.invoke(NetworkConnectivityStatus.Lost)
            }
        }
    }

    private fun getCurrentConnectivityState(): NetworkConnectivityStatus {
        val activeNetwork = systemConnectivityManager.activeNetwork
        val isConnected = systemConnectivityManager.getNetworkCapabilities(activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false

        return if (isConnected) NetworkConnectivityStatus.Available else NetworkConnectivityStatus.Lost
    }
}
