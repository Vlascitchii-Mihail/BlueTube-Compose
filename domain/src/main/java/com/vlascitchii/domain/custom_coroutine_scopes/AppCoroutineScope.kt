package com.vlascitchii.domain.custom_coroutine_scopes

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

interface AppCoroutineScope : CoroutineScope{

    val dispatcher: CoroutineDispatcher

    fun onStart()
    fun onStop()
}