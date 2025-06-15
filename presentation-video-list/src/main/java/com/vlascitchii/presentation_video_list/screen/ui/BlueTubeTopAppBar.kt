package com.vlascitchii.presentation_video_list.screen.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlascitchii.presentation_common.ui.theme.BlueberryBlue
import com.vlascitchii.presentation_video_list.R
import com.vlascitchii.presentation_video_list.util.state.SearchState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlueTubeTopAppBar(
    title: String,
    icon: ImageVector,
    scrollBehavior: TopAppBarScrollBehavior,
    updateSearchState: (newSearchState: SearchState) -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        navigationIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_bluetube_small),
                modifier = Modifier.padding(horizontal = 8.dp),
                tint = BlueberryBlue,
                contentDescription = stringResource(id = R.string.appbar_logo_descr),
            )
        },
        actions = {
            IconButton(
                onClick = { updateSearchState.invoke(SearchState.OPENED) },
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(id = R.string.appbar_search_icon_descr),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            scrolledContainerColor = MaterialTheme.colorScheme.onPrimary
        ),
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun CollapsingAppBarPreview() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    BlueTubeTopAppBar("Test title", Icons.Filled.Search, scrollBehavior) {}
}