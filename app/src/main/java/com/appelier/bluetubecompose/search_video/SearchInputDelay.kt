package com.appelier.bluetubecompose.search_video

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchInputDelay() {

    private var inputDelayJob: Job? = null

    fun setInputDelay(delay: Long, viewModelScope: CoroutineScope, action: () -> Unit) {
        inputDelayJob?.cancel()
        inputDelayJob = viewModelScope.launch {
            delay(delay)
            action.invoke()
        }
    }
}