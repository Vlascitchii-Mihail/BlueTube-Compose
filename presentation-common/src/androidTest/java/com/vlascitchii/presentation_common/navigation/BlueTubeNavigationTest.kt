//package com.vlascitchii.presentation_common.navigation
//
//import androidx.activity.compose.setContent
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.junit4.createAndroidComposeRule
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.performClick
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.appelier.bluetubecompose.BlueTubeApp
//import com.appelier.bluetubecompose.MainActivity
//import com.vlascitchii.presentation_common.utils.NavigationTags
////import dagger.hilt.android.testing.HiltAndroidRule
////import dagger.hilt.android.testing.HiltAndroidTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
////@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//class BlueTubeNavigationTest {
//
//    @get:Rule(order = 0)
//    val composeAndroidTestRule = createAndroidComposeRule(MainActivity::class.java)
//
////    @get:Rule
////    val hiltRule = HiltAndroidRule(this)
//
//    @get:Rule
//    val instantExecutorRule = InstantTaskExecutorRule()
//
//    @Before
//    fun init_app() {
////        hiltRule.inject()
//        composeAndroidTestRule.activity.setContent {
//            BlueTubeApp()
//        }
//    }
//
//    @Test
//    fun app_shows_navigation_and_bottom_nav() {
//        composeAndroidTestRule.onNodeWithTag(com.vlascitchii.presentation_common.utils.NavigationTags.NAVIGATION).assertIsDisplayed()
//        composeAndroidTestRule.onNodeWithTag(com.vlascitchii.presentation_common.utils.NavigationTags.BOTTOM_NAV).assertIsDisplayed()
//    }
//
//    @Test
//    fun button_nav_navigates_to_screens() {
//        composeAndroidTestRule.onNodeWithTag(com.vlascitchii.presentation_common.utils.NavigationTags.VIDEO_LIST_SCREEN).assertIsDisplayed()
//        composeAndroidTestRule.onNodeWithText("Shorts").performClick()
//        composeAndroidTestRule.onNodeWithTag(com.vlascitchii.presentation_common.utils.NavigationTags.SHORTS_SCREEN).assertIsDisplayed()
//        composeAndroidTestRule.onNodeWithText("Settings").performClick()
//        composeAndroidTestRule.onNodeWithTag(com.vlascitchii.presentation_common.utils.NavigationTags.SETTINGS_SCREEN).assertIsDisplayed()
//        composeAndroidTestRule.activityRule.scenario.onActivity { activity ->
//            activity.onBackPressedDispatcher.onBackPressed()
//        }
//        composeAndroidTestRule.onNodeWithTag(com.vlascitchii.presentation_common.utils.NavigationTags.VIDEO_LIST_SCREEN).assertIsDisplayed()
//    }
//}
