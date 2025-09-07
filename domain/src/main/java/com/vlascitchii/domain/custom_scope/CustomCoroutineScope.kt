package com.vlascitchii.domain.custom_scope

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class CustomCoroutineScope(
    override var coroutineContext: CoroutineContext = Dispatchers.IO
) : CoroutineScope {

    fun linkWithParentContextAndGetContext(newContext: CoroutineContext): CoroutineContext {
        if (coroutineContext != newContext) {
            coroutineContext = newContext + Dispatchers.IO
        }
        return coroutineContext
    }
}
