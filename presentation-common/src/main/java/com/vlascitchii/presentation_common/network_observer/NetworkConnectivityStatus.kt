package com.vlascitchii.presentation_common.network_observer

sealed interface NetworkConnectivityStatus {

    data object Available : NetworkConnectivityStatus
    data object Lost : NetworkConnectivityStatus
}
