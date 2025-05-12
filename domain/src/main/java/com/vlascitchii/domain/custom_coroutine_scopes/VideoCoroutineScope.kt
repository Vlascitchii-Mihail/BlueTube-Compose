package com.vlascitchii.domain.custom_coroutine_scopes

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class VideoCoroutineScope(
    var parentJob: Job = SupervisorJob(),
    override var dispatcher: CoroutineDispatcher = Dispatchers.IO,
    override val coroutineContext: CoroutineContext = dispatcher + parentJob
) :  AppCoroutineScope {

    override fun onStart() {
        parentJob = SupervisorJob()
    }

    override fun onStop() {
        parentJob.cancel()
    }
}