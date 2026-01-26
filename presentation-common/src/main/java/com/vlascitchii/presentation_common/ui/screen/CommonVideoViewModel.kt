package com.vlascitchii.presentation_common.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.vlascitchii.domain.custom_scope.CustomCoroutineScope
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.state.UiAction
import com.vlascitchii.presentation_common.ui.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class CommonVideoViewModel<DATA_MODEL: Any, STATE: UiState<Flow<PagingData<DATA_MODEL>>>, ACTION: UiAction>(
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val customCoroutineScope: CustomCoroutineScope,
) : ViewModel() {

    private var _uiStateFlow: MutableStateFlow<UiState<Flow<PagingData<DATA_MODEL>>>> = initState()
    val uiStateFlow: StateFlow<UiState<Flow<PagingData<DATA_MODEL>>>> get() = _uiStateFlow

    private val actionFlow: MutableSharedFlow<ACTION> = MutableSharedFlow<ACTION>()

    private var _connectivityObserver: Flow<NetworkConnectivityStatus> = networkConnectivityObserver.observe()
    val connectivityObserver: Flow<NetworkConnectivityStatus> = _connectivityObserver

    init {
        viewModelScope.launch(customCoroutineScope.coroutineContext) {
            actionFlow.collect { action: ACTION ->
                handleAction(action)
            }
        }
    }

    abstract fun handleAction(action: ACTION)
    abstract fun initState(): MutableStateFlow<UiState<Flow<PagingData<DATA_MODEL>>>>

    fun submitAction(action: ACTION) {
        viewModelScope.launch(customCoroutineScope.coroutineContext) {
            actionFlow.emit(action)
        }
    }

    fun submitUiState(state: STATE) {
        viewModelScope.launch(customCoroutineScope.coroutineContext) {
            _uiStateFlow.emit(state)
        }
    }
}
