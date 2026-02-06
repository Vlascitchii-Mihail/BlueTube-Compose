package com.vlascitchii.presentation_video_list.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.vlascitchii.presentation_common.ui.screen.mvi.CommonMVI
import com.vlascitchii.presentation_common.ui.screen.mvi.PREVIEW_VIDEO_LIST_MVI
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import com.vlascitchii.presentation_common.ui.video_list.state.SearchState
import com.vlascitchii.presentation_common.ui.video_list.state.UiVideoListAction
import com.vlascitchii.presentation_common.ui.video_list.state.VideoListNavigationEvent
import com.vlascitchii.common_ui.R as RCommon
import com.vlascitchii.video_list_screen.R as RPresentationList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlueTubeTopAppBar(
    title: String,
    icon: ImageVector,
    scrollBehavior: TopAppBarScrollBehavior,
    videoListMVI: CommonMVI<UiVideoListAction, VideoListNavigationEvent>,
    modifier: Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shadowElevation = dimensionResource(RCommon.dimen.elevation_medium_16),
        tonalElevation = dimensionResource(RCommon.dimen.elevation_medium_16)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
            },
            navigationIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(RPresentationList.drawable.ic_bluetube_small),
                    modifier = modifier.padding(horizontal = dimensionResource(RCommon.dimen.padding_small_8)),
                    tint = MaterialTheme.colorScheme.onTertiaryFixed,
                    contentDescription = stringResource(id = RPresentationList.string.appbar_logo_descr),
                )
            },
            actions = {

                IconButton(
                    onClick = {
                        videoListMVI.submitAction(UiVideoListAction.ChangeSearchBarAppearance(SearchState.OPENED))
                    },
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(id = RPresentationList.string.appbar_search_icon_descr),
                    )
                }
            },
            scrollBehavior = scrollBehavior,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun CollapsingAppBarPreview() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    BlueTubeComposeTheme {
        Surface {
            BlueTubeTopAppBar("Test title", Icons.Filled.Search, scrollBehavior, PREVIEW_VIDEO_LIST_MVI, Modifier)
        }
    }
}
