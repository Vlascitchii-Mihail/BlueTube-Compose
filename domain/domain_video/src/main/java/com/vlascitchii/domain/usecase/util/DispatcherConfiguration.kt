package com.vlascitchii.domain.usecase.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DispatcherConfiguration @Inject constructor(
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
)
