package com.vlascitchii.presentation_common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.state.UiAction
import com.vlascitchii.presentation_common.ui.state.UiSingleEvent
import com.vlascitchii.presentation_common.ui.state.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class CommonVideoViewModel<DATA_MODEL: Any, STATE: UiState<PagingData<DATA_MODEL>>, ACTION: UiAction, EVENT: UiSingleEvent>(
    private val networkConnectivityObserver: NetworkConnectivityObserver,
) : ViewModel() {

    private var _uiStateFlow: MutableStateFlow<UiState<PagingData<DATA_MODEL>>> = initState()
    val uiStateFlow: StateFlow<UiState<PagingData<DATA_MODEL>>> get() = _uiStateFlow

    private val actionFlow: MutableSharedFlow<ACTION> = MutableSharedFlow<ACTION>()

    private val _navigationSingleEventFlow: Channel<EVENT> = Channel<EVENT>()
    val navigationSingleEventFlow: Flow<EVENT> = _navigationSingleEventFlow.receiveAsFlow()

    private var _connectivityObserver: Flow<NetworkConnectivityStatus> = networkConnectivityObserver.observe()
    val connectivityObserver: Flow<NetworkConnectivityStatus> = _connectivityObserver

    init {
        viewModelScope.launch {
            actionFlow.collect { action: ACTION ->
                handleAction(action)
            }
        }
    }

    abstract fun handleAction(action: ACTION)
    abstract fun initState(): MutableStateFlow<UiState<PagingData<DATA_MODEL>>>

    fun submitAction(action: ACTION) {
        viewModelScope.launch {
            actionFlow.emit(action)
        }
    }

    fun submitUiState(state: STATE) {
        viewModelScope.launch {
            _uiStateFlow.emit(state)
        }
    }

    fun submitNavigationSingleEvent(event: EVENT) {
        viewModelScope.launch {
            _navigationSingleEventFlow.send(event)
        }
    }
}