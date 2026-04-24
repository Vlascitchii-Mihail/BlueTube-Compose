package com.vlascitchii.presentation_common.ui.screen.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlascitchii.presentation_common.ui.state_common.UiAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

abstract class CommonMVIViewModel<ACTION : UiAction>()
    : ViewModel(), CommonMVIBehaviour<ACTION> {

    override val actionFlow: MutableSharedFlow<ACTION> = MutableSharedFlow<ACTION>()

    init {
        viewModelScope.launch() {
            actionFlow.collect { action: ACTION ->
                handleAction(action)
            }
        }
    }

    abstract override fun handleAction(action: ACTION)

    override fun submitAction(action: ACTION) {
        viewModelScope.launch {
            actionFlow.emit(action)
        }
    }
}
