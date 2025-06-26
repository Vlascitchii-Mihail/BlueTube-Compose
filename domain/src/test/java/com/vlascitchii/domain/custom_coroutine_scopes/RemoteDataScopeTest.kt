package com.vlascitchii.domain.custom_coroutine_scopes

import com.vlascitchii.common_test.rule.DispatcherTestRule
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RemoteDataScopeTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private lateinit var job: CompletableJob
    private lateinit var remoteDataScope: VideoCoroutineScope

    @Before
    fun setup() {
        job = SupervisorJob()
        remoteDataScope = VideoCoroutineScope(
            parentJob = job,
            coroutineContext = dispatcherTestRule.testDispatcher
        )
    }

    @Test
    fun `onStart changes the job to a new one`() = runTest {
        remoteDataScope.onStart()
        assertNotEquals(job, remoteDataScope.parentJob)
    }

    @Test
    fun `onStop cancels the job`() {
        remoteDataScope.onStop()
        assertFalse(remoteDataScope.parentJob.isActive)
    }
}