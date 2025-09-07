package com.vlascitchii.presentation_common

import androidx.paging.PagingData
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.ui.state.UiAction
import com.vlascitchii.presentation_common.ui.state.UiSingleEvent
import com.vlascitchii.presentation_common.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.*
import org.junit.Before
import org.mockito.kotlin.mock

class CommonVideoViewModelTest {

    val networkConnectivityObserverMock: NetworkConnectivityObserver = mock()
    private lateinit var commonViewModel: CommonVideoViewModel<YoutubeVideoUiModel, UiState<PagingData<YoutubeVideoUiModel>>, UiAction, UiSingleEvent>
//    private object CommonViewModel : CommonVideoViewModel<YoutubeVideoUiModel, UiState<PagingData<YoutubeVideoUiModel>>, UiAction, UiSingleEvent>(
//        networkConnectivityObserver = mock()
//    ) {
//        override fun handleAction(action: UiAction) {
//            TODO("Not yet implemented")
//        }
//
//        override fun initState(): MutableStateFlow<UiState<PagingData<YoutubeVideoUiModel>>> {
//            TODO("Not yet implemented")
//        }
//    }

    @Before
    fun init() {
        commonViewModel = object : CommonVideoViewModel<YoutubeVideoUiModel, UiState<PagingData<YoutubeVideoUiModel>>, UiAction, UiSingleEvent>(
            networkConnectivityObserver = networkConnectivityObserverMock
        ) {
            override fun handleAction(action: UiAction) {
                TODO("Not yet implemented")
            }

            override fun initState(): MutableStateFlow<UiState<PagingData<YoutubeVideoUiModel>>> =
                MutableStateFlow<UiState<PagingData<YoutubeVideoUiModel>>>(
                    UiState.Loading
                )
        }
    }


}

sealed class TestUiAction : UiAction {
    object TestLoading: TestUiAction()
    data class TestButtonActivness(val isActive: Boolean) : TestUiAction()
    data class TestTextFieldInput(val input: String) : TestUiAction()
}