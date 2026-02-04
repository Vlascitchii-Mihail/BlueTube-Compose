package com.vlascitchii.bluetubecompose.fake.network_connectivity

import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityAbstraction
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeNetworkConnectivity @Inject constructor() : NetworkConnectivityAbstraction {

    override fun observe(): Flow<NetworkConnectivityStatus> = flowOf(NetworkConnectivityStatus.Available)
}
