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
import com.vlascitchii.bluetubecompose.navigation.ScreenType
import com.vlascitchii.presentation_common.R
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import com.vlascitchii.presentation_shorts.screen_shorts.screen.SHORTS_BOTTOM_NAV_NAME
import com.vlascitchii.presentation_video_list.screen.VIDEO_LIST_BOTTOM_NAV_NAME
import com.vlascitchii.presenttion_settings.screen_settings.SETTINGS_BOTTOM_NAV_NAME

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
        VIDEO_LIST_BOTTOM_NAV_NAME,
    ),
    NavigationItem(
        SECOND_INDEX,
        R.drawable.ic_youtube_shorts_logo_24,
        SHORTS_BOTTOM_NAV_NAME,
    ),
    NavigationItem(
        THIRD_INDEX,
        R.drawable.ic_settings_24,
        SETTINGS_BOTTOM_NAV_NAME,
    )
)

@Composable
fun BlueTubeBottomNavigation(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<NavKey> = NavBackStack(ScreenType.VideoList()),
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
                    selected = itemDestinationName == getCurrentNavKeyName(backStack.lastOrNull()),
                    onClick = {
                        when (index) {
                            FIRST_INDEX -> addToBackStackIfDoesNotExistElseMoveAtTheTopExistedOne(backStack, ScreenType.VideoList())
                            SECOND_INDEX -> addToBackStackIfDoesNotExistElseMoveAtTheTopExistedOne(backStack, ScreenType.ShortsScreen)
                            THIRD_INDEX -> addToBackStackIfDoesNotExistElseMoveAtTheTopExistedOne(backStack, ScreenType.SettingsScreen)
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

private fun getCurrentNavKeyName(lastNavigationKey: NavKey?): String {
    return when (lastNavigationKey) {
        is ScreenType.VideoList -> lastNavigationKey.name
        is ScreenType.ShortsScreen -> lastNavigationKey.name
        // Bottom Navigation should display VideoList icon after navigating to the PlayerScreen
        is ScreenType.PlayerScreen ->  VIDEO_LIST_BOTTOM_NAV_NAME
        else -> (lastNavigationKey as ScreenType.SettingsScreen).name
    }
}

private fun checkIfTheDestinationInTheBackStack(backStack: NavBackStack<NavKey>, destinationType: ScreenType): BackStackDestination {
    backStack.forEachIndexed { index: Int, key: NavKey ->
        if ((key as ScreenType).name == destinationType.name) {

            return BackStackDestination(isInBackStack = true, destination = key)
        }
    }

    return BackStackDestination(isInBackStack = false, destination = destinationType)
}

private fun addToBackStackIfDoesNotExistElseMoveAtTheTopExistedOne(backStack: NavBackStack<NavKey>, destinationType: ScreenType) {
    val checkedDestination = checkIfTheDestinationInTheBackStack(backStack, destinationType)
    if (checkedDestination.isInBackStack) {
        moveAtTheTopOfTheBackStack(backStack, checkedDestination)
    }
    else backStack.add(destinationType)

}

private fun moveAtTheTopOfTheBackStack(backStack: NavBackStack<NavKey>, checkedDestination: BackStackDestination) {
    backStack.remove(checkedDestination.destination)
    backStack.add(checkedDestination.destination)
}

private data class BackStackDestination(val isInBackStack: Boolean, val destination: ScreenType)

@Composable
@Preview
private fun BlueTubeBottomNavigationPreview() {
    BlueTubeComposeTheme {
        Surface {
            BlueTubeBottomNavigation()
        }
    }
}
