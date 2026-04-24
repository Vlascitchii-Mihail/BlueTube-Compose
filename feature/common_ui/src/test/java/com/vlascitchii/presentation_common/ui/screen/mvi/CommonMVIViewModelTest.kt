package com.vlascitchii.presentation_common.ui.screen.mvi

import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.presentation_common.ui.state_common.UiAction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CommonMVIViewModelTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    lateinit var commonMviViewModel: CommonMVIViewModel<UiTestAction>

    sealed class UiTestAction: UiAction {
        data class PrintAction(val testAction: String) : UiTestAction()
    }

    @Before
    fun setup() {
        commonMviViewModel = object : CommonMVIViewModel<UiTestAction>() {
            override fun handleAction(action: UiTestAction) {
                when(action) {
                    is UiTestAction.PrintAction -> println("Test action was called with ${action.testAction}")
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `submitAction() emits action event, commonMviViewModel collects it and calls handleAction(action)`() = runTest {
        val expectedActionData = "Test data"
        var actualData = ""

        val listenJob = launch {
            commonMviViewModel.actionFlow.collect { receivedData ->
                actualData = (receivedData as UiTestAction.PrintAction).testAction
                println("Data was received $actualData")
            }
        }

        val submitJob = launch {
            commonMviViewModel.submitAction(UiTestAction.PrintAction(expectedActionData))
        }
        advanceUntilIdle()

        submitJob.cancel()
        listenJob.cancel()
        assertEquals(actualData, expectedActionData)
    }
}
