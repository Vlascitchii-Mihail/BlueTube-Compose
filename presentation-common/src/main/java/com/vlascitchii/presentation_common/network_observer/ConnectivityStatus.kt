package com.vlascitchii.presentation_common.network_observer

sealed interface ConnectivityStatus {

    data object Available : ConnectivityStatus
    data object Lost : ConnectivityStatus
}
