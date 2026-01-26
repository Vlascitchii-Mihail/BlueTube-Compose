package com.vlascitchii.bluetubecompose.navigation.bottom_navigation

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
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.vlascitchii.presentation_common.ui.screen.ScreenType
import com.vlascitchii.presentation_common.R
import com.vlascitchii.presentation_common.ui.screen.SETTINGS
import com.vlascitchii.presentation_common.ui.screen.SHORTS
import com.vlascitchii.presentation_common.ui.screen.VIDEO_LIST
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme

private const val FIRST_INDEX = 0
private const val SECOND_INDEX = 1
private const val THIRD_INDEX = 2

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
    modifier: Modifier = Modifier,
    backStack: NavBackStack<NavKey> = NavBackStack(ScreenType.ShortsScreen),
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
                    selected = itemDestinationName == getCurrentNavKey(backStack.lastOrNull()).name,
                    onClick = {
                        when (index) {
                            FIRST_INDEX -> backStack.add(ScreenType.VideoList())
                            SECOND_INDEX -> backStack.add(ScreenType.ShortsScreen)
                            THIRD_INDEX -> backStack.add(ScreenType.SettingsScreen)
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

private fun getCurrentNavKey(lastNavigationKey: NavKey?): ScreenType {
    return when (lastNavigationKey) {
        is ScreenType.VideoList -> lastNavigationKey
        is ScreenType.ShortsScreen -> lastNavigationKey
        is ScreenType.PlayerScreen -> lastNavigationKey
        else -> lastNavigationKey as ScreenType.SettingsScreen
    }
}


@Composable
@Preview
private fun BlueTubeBottomNavigationPreview() {
    BlueTubeComposeTheme {
        Surface {
            BlueTubeBottomNavigation()
        }
    }
}
