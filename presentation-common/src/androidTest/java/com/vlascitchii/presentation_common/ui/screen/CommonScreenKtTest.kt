package com.vlascitchii.presentation_common.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vlascitchii.presentation_common.R
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.state.UiState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CommonScreenKtTest {

    @get:Rule
    val composeContentTestRule: ComposeContentTestRule = createComposeRule()

    private lateinit var commonErrorDesc: String
    private lateinit var commonLoadDesc: String
    private lateinit var commonSuccessDesc: String
    private val pagingData: PagingData<YoutubeVideoUiModel> = PagingData.from(emptyList<YoutubeVideoUiModel>())
    private val uiStateLoading: UiState<PagingData<YoutubeVideoUiModel>> = UiState.Loading
    private val errorMsg = "Test error"
    private val uiStateError: UiState<PagingData<YoutubeVideoUiModel>> = UiState.Error(errorMsg)
    private val uiStateSuccess: UiState<PagingData<YoutubeVideoUiModel>> = UiState.Success(pagingData)

    private var testUiState: UiState<PagingData<YoutubeVideoUiModel>> by mutableStateOf(uiStateLoading)

    @Composable
    fun TestScreen() {
        Box(modifier = Modifier.fillMaxSize().semantics { contentDescription = commonSuccessDesc})
    }

    @Before
    fun init() {
        composeContentTestRule.setContent {
            commonSuccessDesc = stringResource(R.string.common_success_compos_desc)
            commonErrorDesc = stringResource(R.string.common_error_compos_desc)
            commonLoadDesc = stringResource(R.string.common_loading_compose_desc)

            CommonScreen(testUiState) {
                TestScreen()
            }
        }
    }


    @Test
    fun fun_CommonScreen_shows_load_Loading_comp_onLoad_state() {
        with(composeContentTestRule) {
            onNodeWithContentDescription(commonLoadDesc).assertIsDisplayed()
        }
    }

    @Test
    fun fun_CommonScreen_shows_Error_comp_onError_state() {
        testUiState = uiStateError
        with(composeContentTestRule) {
            onNodeWithContentDescription(commonErrorDesc).assertIsDisplayed()
        }
    }

    @Test
    fun fun_CommonScreen_shows_onSuccess_comp_on_UiState_success() {
        testUiState = uiStateSuccess
        with(composeContentTestRule) {
            onNodeWithContentDescription(commonSuccessDesc).assertIsDisplayed()
        }
    }
}
