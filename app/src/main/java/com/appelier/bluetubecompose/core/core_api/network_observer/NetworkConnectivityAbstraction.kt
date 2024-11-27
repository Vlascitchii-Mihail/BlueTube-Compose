package com.appelier.bluetubecompose.core.core_api.network_observer

import kotlinx.coroutines.flow.Flow

interface NetworkConnectivityAbstraction {

    fun observe(): Flow<ConnectivityStatus>
}