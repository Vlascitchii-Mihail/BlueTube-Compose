package com.vlascitchii.presentation_common.ui.global_snackbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vlascitchii.presentation_common.R
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ObserveAsEventsKtTest {

    @get:Rule
    val composeContentTestRule = createComposeRule()

    private lateinit var snackbarContentDesc: String
    private val boxContentDesc: String = "Test box Description"
    val snackBarMessage = "Test message"
    val innerSnackBarMessage = "Test message"
    val snackbarTestActionMSG = "Test action"

    @Before
    fun init() {
        composeContentTestRule.setContent {

            snackbarContentDesc = stringResource(R.string.snackbar_description)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .semantics { contentDescription = boxContentDesc },
                contentAlignment = Alignment.BottomCenter
            ) {
                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }

                ObserveAsEvents(
                    flow = SnackBarController.events,
                    key1 = snackbarHostState,
                    onEvent = { event: SnackBarEvent ->
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()

                            val result = snackbarHostState.showSnackbar(
                                message = event.message,
                                actionLabel = event.action?.name,
                                duration = SnackbarDuration.Long
                            )

                            if (result == SnackbarResult.ActionPerformed) {
                                event.action?.action?.invoke()
                            }
                        }
                    }
                )

                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .semantics { contentDescription = snackbarContentDesc }
                )
            }

            LaunchedEffect(Unit) {
                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message = snackBarMessage,
                        action = SnackBarAction(name = snackbarTestActionMSG, action = {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(message = innerSnackBarMessage)
                            )
                        })
                    )
                )
            }
        }
    }

    @Test
    fun snackbar_appears_with_message() {
        with(composeContentTestRule) {
            onNodeWithContentDescription(snackbarContentDesc).assertIsDisplayed()
            onNodeWithText(snackBarMessage).assertIsDisplayed()
            onNodeWithContentDescription(snackbarContentDesc).assert(hasAnyDescendant(hasText(snackBarMessage)))
        }
    }

    @Test
    fun snackbar_action_message_is_visible() {
        with(composeContentTestRule) {
            onNodeWithContentDescription(snackbarContentDesc).assertIsDisplayed()
            onNodeWithContentDescription(snackbarContentDesc).assert(hasAnyDescendant(hasClickAction()))
            onNodeWithContentDescription(snackbarContentDesc).assert(hasAnyDescendant(hasText(innerSnackBarMessage)))
        }
    }
}
