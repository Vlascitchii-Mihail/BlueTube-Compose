package com.appelier.bluetubecompose.core.core_api.network_observer

sealed interface ConnectivityStatus {

    data object Available : ConnectivityStatus
    data object Lost : ConnectivityStatus
}