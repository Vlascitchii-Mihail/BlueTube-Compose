package com.vlascitchii.presentation_common.ui.bottom_navigation

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.vlascitchii.presentation_common.R
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme

private const val FIRST_INDEX = 0
private const val SECOND_INDEX = 1
private const val THIRD_INDEX = 2
const val VIDEO_LIST = "Video list"
const val SHORTS: String = "Shorts"
const val SETTINGS: String = "Settings"

private data class NavigationItem(
    val index: Int,
    val vectorResourceId: Int,
    val screenName: String,
)

private val navItemsList = listOf(
    NavigationItem(
        FIRST_INDEX,
        R.drawable.ic_house_24,
        VIDEO_LIST,
    ),
    NavigationItem(
        SECOND_INDEX,
        R.drawable.ic_youtube_shorts_logo_24,
        SHORTS,
    ),
    NavigationItem(
        THIRD_INDEX,
        R.drawable.ic_settings_24,
        SETTINGS,
    )
)

@Composable
fun BlueTubeBottomNavigation(
    currentDestinationName: String = VIDEO_LIST,
    navigateToVideo: () -> Unit = {},
    navigateToShorts: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    val bottomNavigationDescription = stringResource(R.string.bottom_navigation_description)

    Surface(
        shadowElevation = dimensionResource(R.dimen.elevation_medium_16),
        tonalElevation = dimensionResource(R.dimen.elevation_medium_16)
    ) {
        NavigationBar(
            modifier = modifier
                .height(dimensionResource(R.dimen.height_large_extra_72))
                .semantics { contentDescription = bottomNavigationDescription }
        ) {
            navItemsList.forEachIndexed { index, navItem ->
                val itemDestinationName = navItem.screenName
                NavigationBarItem(
                    selected = itemDestinationName == currentDestinationName,
                    onClick = {
                        when (index) {
                            FIRST_INDEX -> navigateToVideo.invoke()
                            SECOND_INDEX -> navigateToShorts.invoke()
                            THIRD_INDEX -> navigateToSettings.invoke()
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(navItem.vectorResourceId),
                            contentDescription = navItem.screenName
                        )
                    },
                    modifier = modifier.semantics { contentDescription = itemDestinationName }
                )
            }
        }
    }
}


@Composable
@Preview
private fun BlueTubeBottomNavigationPreview() {
    BlueTubeComposeTheme {
        Surface {
            BlueTubeBottomNavigation(
                SHORTS,
                {},
                {},
                {}
            )
        }
    }
}
