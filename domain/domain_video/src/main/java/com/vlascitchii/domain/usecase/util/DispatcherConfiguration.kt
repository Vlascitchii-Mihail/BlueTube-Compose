package com.vlascitchii.domain.usecase.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DispatcherConfiguration(
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
)
