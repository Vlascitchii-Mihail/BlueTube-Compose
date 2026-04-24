package com.vlascitchii.presentation_common.ui.screen.mvi

import kotlinx.coroutines.flow.MutableSharedFlow

interface CommonMVIBehaviour<ACTION> {
    val actionFlow: MutableSharedFlow<ACTION>

    fun handleAction(action: ACTION)

    fun submitAction(action: ACTION)
}
