package com.vlascitchii.presentation_common.network_observer

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.vlascitchii.common_test.rule.DispatcherTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.KArgumentCaptor
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkConnectivityObserverTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val systemConnectivityManager: ConnectivityManager = mock()
    private val networkConnectivityObserver =  NetworkConnectivityObserver(systemConnectivityManager)
    private lateinit var callbackCaptor: KArgumentCaptor<ConnectivityManager.NetworkCallback>
    private val mockNetwork: Network = mock()
    private lateinit var results: MutableList<NetworkConnectivityStatus>

    @Before
    fun init() {
        results = mutableListOf<NetworkConnectivityStatus>()
        callbackCaptor = argumentCaptor<ConnectivityManager.NetworkCallback>()

        doNothing().whenever(systemConnectivityManager)
            .registerDefaultNetworkCallback(callbackCaptor.capture())
    }

    @Test
    fun fun_getCurrentConnectivityState_returns_Available() = runTest {
        setup_Available_getCurrentConnectivityState_response()

        val job = launch {
            networkConnectivityObserver.observe().toList(results)
        }
        advanceUntilIdle()

        job.cancel()
        assertEquals(NetworkConnectivityStatus.Available, results.first())
    }

    private fun setup_Available_getCurrentConnectivityState_response() {
        val mockCapabilities: NetworkCapabilities = mock()
        whenever(systemConnectivityManager.activeNetwork).thenReturn(mockNetwork)
        whenever(systemConnectivityManager.getNetworkCapabilities(mockNetwork)).thenReturn(mockCapabilities)
        whenever(mockCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)).thenReturn(true)
    }

    @Test
    fun fun_getCurrentConnectivityState_returns_Lost() = runTest {
        val job = launch {
            networkConnectivityObserver.observe().toList(results)
        }
        advanceUntilIdle()

        job.cancel()
        assertEquals(NetworkConnectivityStatus.Lost, results.first())
    }

    @Test
    fun networkConnectivityObserver_returns_network_status_Available() = runTest {
        val job = launch {
            drop_First_getCurrentConnectivityState_response()
        }
        advanceUntilIdle()

        val capturedCallback = callbackCaptor.firstValue
        capturedCallback.onAvailable(mockNetwork)
        advanceUntilIdle()

        job.cancel()
        assertEquals(NetworkConnectivityStatus.Available, results.first())
    }

    private suspend fun drop_First_getCurrentConnectivityState_response() {
        networkConnectivityObserver.observe().drop(1).toList(results)
    }

    @Test
    fun networkConnectivityObserver_returns_network_status_Lost() = runTest {
        val job = launch {
            networkConnectivityObserver.observe().toList(results)
        }
        advanceUntilIdle()

        val capturedCallback = callbackCaptor.firstValue
        capturedCallback.onLost(mockNetwork)
        advanceUntilIdle()
        println(results)

        job.cancel()
        assertEquals(NetworkConnectivityStatus.Lost, results.first())
    }
}
