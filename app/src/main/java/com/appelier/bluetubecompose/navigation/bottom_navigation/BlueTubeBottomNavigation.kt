package com.appelier.bluetubecompose.navigation.bottom_navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.navigation.ScreenType
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import com.vlascitchii.presentation_common.utils.NavigationTags.BOTTOM_NAV

private const val BOTTOM_NAV_APPEARANCE = 500

private const val FIRST_ICON_INDEX = 0
private const val SECOND_ICON_INDEX = 1
private const val THIRD_ICON_INDEX = 2

private data class NavigationItem(
    val index: Int,
    val vectorResourceId: Int,
    val screenDescriptionId: Int,
    val screen: ScreenType
)

private val navItemsList = listOf(
    NavigationItem(
        FIRST_ICON_INDEX,
        R.drawable.ic_house_24,
        R.string.video_list_screen,
        ScreenType.VideoList
    ),
    NavigationItem(
        SECOND_ICON_INDEX,
        R.drawable.ic_youtube_shorts_logo_24,
        R.string.shorts_screen,
        ScreenType.ShortsScreen
    ),
    NavigationItem(
        THIRD_ICON_INDEX,
        R.drawable.ic_settings_24,
        R.string.settings_screen,
        ScreenType.SettingsScreen
    )
)

@Composable
fun BlueTubeBottomNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val bottomNavigationVisibilityState = rememberSaveable {
        mutableStateOf(BottomNavigationVisibilityState.VISIBLE)
    }

    DisposableEffect(Unit) {
        val navigationCallback =
            NavController.OnDestinationChangedListener { controller, destination, arguments ->
                val containsPlayerScreen =
                    destination.route?.contains(ScreenType.PlayerScreen::class.java.simpleName)
                val state = when (true) {
                    containsPlayerScreen -> BottomNavigationVisibilityState.INVISIBLE
                    else -> BottomNavigationVisibilityState.VISIBLE
                }

                bottomNavigationVisibilityState.value = state
            }
        navController.addOnDestinationChangedListener(navigationCallback)
        onDispose { navController.removeOnDestinationChangedListener(navigationCallback) }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    AnimatedVisibility(
        visible = bottomNavigationVisibilityState.value == BottomNavigationVisibilityState.VISIBLE,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = BOTTOM_NAV_APPEARANCE)
        ) + fadeIn(animationSpec = tween(durationMillis = BOTTOM_NAV_APPEARANCE)),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = BOTTOM_NAV_APPEARANCE)
        ) + fadeOut(animationSpec = tween(durationMillis = BOTTOM_NAV_APPEARANCE))
    ) {
        BottomNavigation(modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .testTag(BOTTOM_NAV)
            .fillMaxWidth()
        ) {
            navItemsList.forEach { navItem ->
                val isSelected = currentDestination?.route?.contains(navItem.screen.toString()) ?: false

                BottomNavigationItem(
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            navController.navigate(navItem.screen) {
                                popUpToStartDestination(navController)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(navItem.vectorResourceId),
                            contentDescription = stringResource(id = navItem.screenDescriptionId),
                        )
                    },
                    label = {
                        Text(
                            text = navItem.screen.name,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    },
                    unselectedContentColor = MaterialTheme.colorScheme.onPrimary,
                    selectedContentColor = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

private fun NavOptionsBuilder.popUpToStartDestination(navController: NavHostController) {
    popUpTo(navController.graph.findStartDestination().id)
    launchSingleTop = true
}

@Composable
@Preview
private fun BlueTubeBottomNavigationPreview() {
    BlueTubeComposeTheme {
        BlueTubeBottomNavigation(rememberNavController())
    }
}