package com.vlascitchii.presentation_common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.compose.LazyPagingItems
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vlascitchii.domain.util.UseCaseException
import com.vlascitchii.presentation_common.R
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever


@RunWith(AndroidJUnit4::class)
class PagerContentManagerKtTest {

    @get:Rule
    val composeContentTestRule: ComposeContentTestRule = createComposeRule()

    private val lazyPagingItem: LazyPagingItems<YoutubeVideoUiModel> = mock()

    private lateinit var commonSuccessDesc: String
    private lateinit var circularProgressIndicatorDescription: String
    private lateinit var paginationError: String

    private val notLoadingLoadState = LoadState.NotLoading(true)
    private val loadingLoadState = LoadState.Loading
    private val useCaseError = UseCaseException.VideoListLoadException(RuntimeException())
    private val errorLoadState = LoadState.Error(useCaseError)

    @Composable
    private fun ContentPage() {
        Box(modifier = Modifier.fillMaxSize().semantics { contentDescription = commonSuccessDesc })
    }

    private fun getLoadState(loadState: LoadState): CombinedLoadStates {
        return CombinedLoadStates(
            prepend = LoadState.Loading,
            refresh = loadState,
            append = LoadState.NotLoading(endOfPaginationReached = true),
            source = LoadStates(
                refresh = loadState,
                prepend = LoadState.NotLoading(endOfPaginationReached = true),
                append = LoadState.Loading
            ),
            mediator = null
        )
    }

    @Test
    fun pagerContentManager_shows_content_list_when_loadState_is_NotLoading() {
        val loadState = getLoadState(notLoadingLoadState)
        whenever(lazyPagingItem.loadState).thenReturn(loadState)

        composeContentTestRule.setContent {
            commonSuccessDesc = stringResource(R.string.common_success_compos_desc)

            PagerContentManager(
                lazyPagingItem,
                { ContentPage() }
            )
        }

        with(composeContentTestRule) {
            onNodeWithContentDescription(commonSuccessDesc).assertIsDisplayed()
        }
    }

    @Test
    fun pagerContentManager_shows_CircularProgressIndicator_when_loadState_is_Loading() {
        val loadState = getLoadState(loadingLoadState)
        whenever(lazyPagingItem.loadState).thenReturn(loadState)

        composeContentTestRule.setContent {
            circularProgressIndicatorDescription = stringResource(R.string.circular_progress_indicator)

            PagerContentManager(
                lazyPagingItem,
                { ContentPage() }
            )
        }

        with(composeContentTestRule) {
            onNodeWithContentDescription(circularProgressIndicatorDescription).assertIsDisplayed()
        }
    }

    @Test
    fun pagerContentManager_shows_PaginationErrorItem_when_loadState_is_Error() {
        val loadState = getLoadState(errorLoadState)
        whenever(lazyPagingItem.loadState).thenReturn(loadState)

        composeContentTestRule.setContent {
            paginationError = stringResource(R.string.paging_error_msg_txt)

            PagerContentManager(
                lazyPagingItem,
                { ContentPage() }
            )
        }

        with(composeContentTestRule) {
            onNodeWithContentDescription(paginationError).assertIsDisplayed()
        }
    }
}