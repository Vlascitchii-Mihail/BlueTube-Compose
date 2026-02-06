package com.vlascitchii.presentation_common.ui.screen.mvi

import com.vlascitchii.domain.custom_scope.CustomCoroutineScope
import com.vlascitchii.presentation_common.ui.state_common.UiAction
import com.vlascitchii.presentation_common.ui.state_common.UiSingleEvent
import com.vlascitchii.presentation_common.ui.video_list.state.UiVideoListAction
import com.vlascitchii.presentation_common.ui.video_list.state.VideoListNavigationEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

val PREVIEW_VIDEO_LIST_MVI: CommonMVI<UiVideoListAction, VideoListNavigationEvent> =
    object : CommonMVI<UiVideoListAction, VideoListNavigationEvent>() {
        override fun handleAction(action: UiVideoListAction) {}
        override fun handleNavigationEvent(singleEvent: VideoListNavigationEvent) {}
    }

abstract class CommonMVI<ACTION : UiAction, EVENT : UiSingleEvent>(
    private val coroutineScope: CoroutineScope = CustomCoroutineScope()
) {

    private val actionFlow: MutableSharedFlow<ACTION> = MutableSharedFlow<ACTION>()
    private val singleEventChannel: Channel<EVENT> = Channel<EVENT>()

    init {
        coroutineScope.launch {
            actionFlow.collect { action: ACTION ->
               handleAction(action)
            }
        }

        coroutineScope.launch {
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
