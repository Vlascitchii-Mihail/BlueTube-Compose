package com.vlascitchii.presentation_common.ui.global_snackbar

import com.vlascitchii.common_test.rule.DispatcherTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SnackBarControllerTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    val testMessage = "Test message"
    val testName = "TestName"
    val snakbarAction = SnackBarAction(
        name = testName,
        action = { }
    )
    val testEvent = SnackBarEvent(
        message = testMessage,
        action =  snakbarAction
    )

    @Test
    fun `sendEvent updates channel`() = runTest {
        val job = launch {
            SnackBarController.sendEvent(testEvent)
        }
        advanceUntilIdle()

        assertEquals(testMessage, SnackBarController.events.first().message)
        job.cancel()
    }

    @Test
    fun `sendEvent updates channel with an action`() = runTest {
        val job = launch {
            SnackBarController.sendEvent(testEvent)
        }
        advanceUntilIdle()

        assertEquals(testName,  SnackBarController.events.first().action?.name)
        job.cancel()
    }
}