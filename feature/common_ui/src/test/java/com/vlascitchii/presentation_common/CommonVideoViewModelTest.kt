package com.vlascitchii.presentation_common

import androidx.paging.PagingData
import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.domain.custom_scope.CustomCoroutineScope
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.ui.screen.CommonVideoViewModel
import com.vlascitchii.presentation_common.ui.state.UiAction
import com.vlascitchii.presentation_common.ui.state.UiSingleEvent
import com.vlascitchii.presentation_common.ui.state.UiState
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock

sealed class TestUiAction : UiAction {
    object TestLoading : TestUiAction()
    data class TestSearch(val isActive: Boolean) : TestUiAction()
    data class TestTextFieldInput(val input: String) : TestUiAction()
}

@OptIn(ExperimentalCoroutinesApi::class)
class CommonVideoViewModelTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    val networkConnectivityObserverMock: NetworkConnectivityObserver = mock()
    private lateinit var commonViewModel: CommonVideoViewModel<YoutubeVideoUiModel, UiState<Flow<PagingData<YoutubeVideoUiModel>>>, UiAction>
    private val customCoroutineScope: CustomCoroutineScope =
        CustomCoroutineScope(dispatcherTestRule.testDispatcher)
    val uiStateSuccess = UiState.Success<Flow<PagingData<YoutubeVideoUiModel>>>(
        flowOf(PagingData.from(emptyList<YoutubeVideoUiModel>()))
    )

    private val testErrorMessage = "Test Error Message"
    val uiStateError = UiState.Error<Flow<PagingData<YoutubeVideoUiModel>>>(
        testErrorMessage
    )

    @Before
    fun init() {
        commonViewModel = object :
            CommonVideoViewModel<YoutubeVideoUiModel, UiState<Flow<PagingData<YoutubeVideoUiModel>>>, UiAction>(
                networkConnectivityObserver = networkConnectivityObserverMock,
                customCoroutineScope
            ) {
            override fun handleAction(action: UiAction) {
                when (action) {
                    is TestUiAction.TestLoading -> { commonViewModel.submitUiState(uiStateSuccess) }
                    is TestUiAction.TestSearch -> { commonViewModel.submitUiState(uiStateError )}
                    is TestUiAction.TestTextFieldInput -> { commonViewModel.submitUiState(uiStateSuccess) }
                }
            }

            override fun initState(): MutableStateFlow<UiState<Flow<PagingData<YoutubeVideoUiModel>>>> =
                MutableStateFlow<UiState<Flow<PagingData<YoutubeVideoUiModel>>>>(
                    UiState.Loading
                )
        }

    }

    @Test
    fun `submitUiState() updates _uiStateFlow`() = runTest {
        commonViewModel.submitUiState(uiStateSuccess)
        advanceUntilIdle()
        assertEquals(
            uiStateSuccess.data.first(),
            (commonViewModel.uiStateFlow.value as UiState.Success).data.first()
        )
    }


    @Test
    fun `submitAction() calls handleAction()`() = runTest {
        val testSearch = TestUiAction.TestSearch(true)
        val testTextFieldInput = TestUiAction.TestTextFieldInput("")

        commonViewModel.submitAction(TestUiAction.TestLoading)
        advanceUntilIdle()
        assertEquals(uiStateSuccess, commonViewModel.uiStateFlow.first())

        commonViewModel.submitAction(testSearch)
        advanceUntilIdle()
        assertEquals(uiStateError, commonViewModel.uiStateFlow.first())

        commonViewModel.submitAction(testTextFieldInput)
        advanceUntilIdle()
        assertEquals(uiStateSuccess, commonViewModel.uiStateFlow.first())
    }
}
