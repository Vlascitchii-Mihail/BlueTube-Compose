package com.vlascitchii.presentation_common.network_observer

import kotlinx.coroutines.flow.Flow

interface NetworkConnectivityAbstraction {

    fun observe(): Flow<ConnectivityStatus>
}