package com.appelier.bluetubecompose.ui.views

import androidx.compose.foundation.background
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.navigation.Screen
import com.appelier.bluetubecompose.ui.theme.BlueTubeComposeTheme
import com.appelier.bluetubecompose.utils.Tags

private const val FIRST_ICON_INDEX = 0
private const val SECOND_ICON_INDEX = 1
private const val THIRD_ICON_INDEX = 2

private data class NavigationItem(
    val index: Int,
    val vectorResourceId: Int,
    val screenDescriptionId: Int,
    val screen: Screen
)

private val navItemsList = listOf(
    NavigationItem(FIRST_ICON_INDEX, R.drawable.ic_house_24, R.string.video_list_screen_descr, Screen.VideoList),
    NavigationItem(SECOND_ICON_INDEX, R.drawable.ic_youtube_shorts_logo_24, R.string.shorts_screen_descr, Screen.ShortsScreen),
    NavigationItem(THIRD_ICON_INDEX, R.drawable.ic_settings_24, R.string.settings_screen_descr, Screen.SettingsScreen)
)

@Composable
private fun NavigationItem.getScreenSelectedState(currentDestination: NavDestination?): Boolean {
    return currentDestination?.hierarchy?.any {
        it.route?.contains(stringResource(id = this.screenDescriptionId)) ?: false
    } == true
}

@Composable
fun TubeBottomNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(modifier = Modifier.testTag(Tags.BOTTOM_NAV)) {
        navItemsList.forEach { navItem ->
            val isSelected = navItem.getScreenSelectedState(currentDestination = currentDestination)

            BottomNavigationItem(
                modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                selected = isSelected,
                onClick = {
                    if (!isSelected){
                        navController.navigate(navItem.screen) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
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
                    Text(text = navItem.screen.name)
                },
                unselectedContentColor = MaterialTheme.colorScheme.onPrimary,
                selectedContentColor = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
@Preview
private fun BlueTubeBottomNavigationPreview() {
    BlueTubeComposeTheme {
        TubeBottomNavigation(rememberNavController())
    }
}