package com.vlascitchii.presentation_common.ui.screen

import androidx.paging.PagingData
import com.vlascitchii.presentation_common.ui.state.UiAction
import com.vlascitchii.presentation_common.ui.state.UiSingleEvent
import com.vlascitchii.presentation_common.ui.state.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class CommonMVI<DATA_MODEL : Any, STATE : UiState<Flow<PagingData<DATA_MODEL>>>, ACTION : UiAction, EVENT : UiSingleEvent>(
    private val coroutineScope: CoroutineScope
) {

    private val actionFlow: MutableSharedFlow<ACTION> = MutableSharedFlow<ACTION>()
    private val singleEventChannel: Channel<EVENT> = Channel<EVENT>()

    init {
        coroutineScope.launch {
            actionFlow.collect { action: ACTION ->
               handleAction(action)
            }
            singleEventChannel.receiveAsFlow().collectLatest { event: EVENT ->
               handleNavigationEvent(event)
            }
        }
    }

    protected abstract fun handleAction(action: ACTION)
    protected abstract fun handleNavigationEvent(singleEvent: EVENT)

    fun submitAction(action: ACTION) {
        coroutineScope.launch {
            actionFlow.emit(action)
        }
    }

    fun submitSingleNavigationEvent(event: EVENT) {
        coroutineScope.launch {
            singleEventChannel.send(event)
        }
    }
}
