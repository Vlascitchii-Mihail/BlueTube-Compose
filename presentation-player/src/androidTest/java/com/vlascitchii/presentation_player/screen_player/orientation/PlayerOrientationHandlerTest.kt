package com.vlascitchii.presentation_player.screen_player.orientation

import android.app.Activity
import androidx.paging.PagingData
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel.Companion.PREVIEW_VIDEO_LIST
import com.vlascitchii.presentation_common.ui.screen.mvi.CommonMVI
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_player.databinding.FragmentPlayVideoBinding
import com.vlascitchii.presentation_player.screen_player.OrientationState
import com.vlascitchii.presentation_player.screen_player.TestActivity
import com.vlascitchii.presentation_player.screen_player.state.PlayerActionState
import com.vlascitchii.presentation_player.screen_player.state.PlayerNavigationEvent
import com.vlascitchii.presentation_player.screen_player.state.PlayerState
import com.vlascitchii.ui_test.autorotation_system_setting.AUTOROTATION_OFF
import com.vlascitchii.ui_test.autorotation_system_setting.AUTOROTATION_ON
import com.vlascitchii.ui_test.autorotation_system_setting.landscapeOrientation
import com.vlascitchii.ui_test.autorotation_system_setting.portraitOrientation
import com.vlascitchii.ui_test.autorotation_system_setting.reverseLandscapeOrientation
import com.vlascitchii.ui_test.autorotation_system_setting.setAutorotationEnabledValue
import com.vlascitchii.ui_test.autorotation_system_setting.unspecifiedOrientation
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.atMost
import org.mockito.kotlin.never
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify

@RunWith(AndroidJUnit4::class)
class PlayerOrientationHandlerTest {

    private lateinit var scenario: ActivityScenario<TestActivity>
    private lateinit var playerOrientationHandler: PlayerOrientationHandler
    private lateinit var testActivity: Activity
    private lateinit var binding: FragmentPlayVideoBinding
    private lateinit var playerStateFlow: MutableStateFlow<PlayerState>

    private val fullScreenIntent = PlayerActionState.UpdatePlayerOrientationStateAction(OrientationState.FULL_SCREEN)
    private val portraitIntent = PlayerActionState.UpdatePlayerOrientationStateAction(OrientationState.PORTRAIT)
    private val lockOrientationIntent = PlayerActionState.ApproveOrientationChange(false)
    private val unlockOrientationIntent = PlayerActionState.ApproveOrientationChange(true)

    private val pagingUiData: Flow<PagingData<YoutubeVideoUiModel>> =
        flowOf(PagingData.Companion.from(PREVIEW_VIDEO_LIST))
    private val successUiState: UiState<Flow<PagingData<YoutubeVideoUiModel>>> =
        UiState.Success(pagingUiData)
    private val playerMVI: CommonMVI<PlayerActionState, PlayerNavigationEvent> = spy(
        object : CommonMVI<PlayerActionState, PlayerNavigationEvent>() {
            override fun handleAction(action: PlayerActionState) {
                when (action) {
                    is PlayerActionState.GetRelatedVideosAction -> {}
                    is PlayerActionState.UpdatePlayStateAction -> {}
                    is PlayerActionState.UpdatePlaybackPositionAction -> {}
                    is PlayerActionState.UpdatePlayerOrientationStateAction -> {
                        playerStateFlow.value = PlayerState(
                            relatedVideoState = successUiState,
                            playerOrientationState = action.orientationState
                        )
                    }

                    is PlayerActionState.ApproveOrientationChange -> {
                        playerStateFlow.value = PlayerState(
                            relatedVideoState = successUiState,
                            isOrientationApproved = action.isOrientationApproved
                        )
                    }
                }
            }

            override fun handleNavigationEvent(singleEvent: PlayerNavigationEvent) {
                println("handleNavigationEvent() waa called")
            }
        }
    )

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(TestActivity::class.java)
            .onActivity { activity: Activity ->
                testActivity = activity
                binding = FragmentPlayVideoBinding.inflate(activity.layoutInflater)
                playerStateFlow = MutableStateFlow(PlayerState(isOrientationApproved = true))

                playerOrientationHandler = PlayerOrientationHandler(
                    binding = binding,
                    activity = activity,
                    playerMVI = playerMVI,
                    playerStateFlow = playerStateFlow,
                )
            }
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun `changeToPortraitOrientation calls playerMVI and changes the orientation to portrait`() {
        setAutorotationEnabledValue(AUTOROTATION_ON)
        testActivity.requestedOrientation = landscapeOrientation

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenScreenAutoRotateSettingIsEnabled(
                ACCELEROMETER_PORTRAIT_ORIENTATION
            )
        }

        verify(playerMVI).submitAction(portraitIntent)
        assertTrue(testActivity.requestedOrientation == portraitOrientation)
    }

    @Test
    fun `changeToLandscapeOrientation calls playerMVI and changes the orientation to landscape`() {
        setAutorotationEnabledValue(AUTOROTATION_ON)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenScreenAutoRotateSettingIsEnabled(
                ACCELEROMETER_LANDSCAPE_ORIENTATION
            )
        }

        verify(playerMVI).submitAction(fullScreenIntent)
        assertTrue(testActivity.requestedOrientation == landscapeOrientation)
    }

    @Test
    fun `changeToReverseLandscapeOrientation calls playerMVI and changes the orientation to landscape reverse`() {
        setAutorotationEnabledValue(AUTOROTATION_ON)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenScreenAutoRotateSettingIsEnabled(
                ACCELEROMETER_LANDSCAPE_REVERSE_ORIENTATION
            )
        }

        verify(playerMVI).submitAction(fullScreenIntent)
        assertTrue(testActivity.requestedOrientation == reverseLandscapeOrientation)
    }

    @Test
    fun `outFromFullScreenAndLockPortraitOrientation changes to portrait orientation`() {
        setAutorotationEnabledValue(AUTOROTATION_ON)

        testActivity.requestedOrientation = landscapeOrientation
        playerOrientationHandler.outFromFullScreenSetStaticPortraitOrientation()

        verify(playerMVI).submitAction(portraitIntent)
        assertTrue(testActivity.requestedOrientation == portraitOrientation)
    }

    @Test
    fun `setSensorOrientation sets the sensor orientation`() {
        playerOrientationHandler.setSensorOrientation()
        assertTrue(testActivity.requestedOrientation == unspecifiedOrientation)
    }

    @Test
    fun `rotateWhenScreenAutoRotateSettingIsEnabled calls orientation change from portrait to landscape when Autorotation setting is on`() {
        setAutorotationEnabledValue(AUTOROTATION_ON)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenScreenAutoRotateSettingIsEnabled(
                ACCELEROMETER_LANDSCAPE_ORIENTATION
            )
        }

        verify(playerMVI).submitAction(fullScreenIntent)
        println("Current orientation = ${testActivity.requestedOrientation}")
        assertTrue(testActivity.requestedOrientation == landscapeOrientation)
    }

    @Test
    fun `rotateWhenScreenAutoRotateSettingIsEnabled calls orientation change from landscape to portrait when Autorotation setting is on`() {
        setAutorotationEnabledValue(AUTOROTATION_ON)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenScreenAutoRotateSettingIsEnabled(
                ACCELEROMETER_PORTRAIT_ORIENTATION
            )
        }

        verify(playerMVI).submitAction(portraitIntent)
        println("Current orientation = ${testActivity.requestedOrientation}")
        assertTrue(testActivity.requestedOrientation == portraitOrientation)
    }

    @Test
    fun `rotateWhenScreenAutoRotateSettingIsEnabled calls orientation change from portrait to reverseLandscape when Autorotation setting is on`() {
        setAutorotationEnabledValue(AUTOROTATION_ON)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenScreenAutoRotateSettingIsEnabled(
                ACCELEROMETER_LANDSCAPE_REVERSE_ORIENTATION
            )
        }

        verify(playerMVI).submitAction(fullScreenIntent)
        println("Current orientation = ${testActivity.requestedOrientation}")
        assertTrue(testActivity.requestedOrientation == reverseLandscapeOrientation)
    }

    @Test
    fun `rotateWhenScreenAutoRotateSettingIsEnabled does not call any orientation change from portrait to landscape when Autorotation setting is off`() {
        setAutorotationEnabledValue(AUTOROTATION_OFF)
        testActivity.requestedOrientation = portraitOrientation

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenScreenAutoRotateSettingIsEnabled(
                ACCELEROMETER_LANDSCAPE_ORIENTATION
            )
        }

        verify(playerMVI, never()).submitAction(any<PlayerActionState.UpdatePlayerOrientationStateAction>())
        println("Current orientation = ${testActivity.requestedOrientation}")
        assertTrue(testActivity.requestedOrientation == portraitOrientation)
    }

    @Test
    fun `rotateWhenScreenAutoRotateSettingIsEnabled does not call any orientation change from landscape to portrait when Autorotation setting is off`() {
        setAutorotationEnabledValue(AUTOROTATION_OFF)

        testActivity.requestedOrientation = landscapeOrientation

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenScreenAutoRotateSettingIsEnabled(
                ACCELEROMETER_PORTRAIT_ORIENTATION
            )
        }

        verify(playerMVI, never()).submitAction(any<PlayerActionState.UpdatePlayerOrientationStateAction>())
        println("Current orientation = ${testActivity.requestedOrientation}")
        assertTrue(testActivity.requestedOrientation == landscapeOrientation)
    }

    @Test
    fun `rotateWhenScreenAutoRotateSettingIsEnabled does not call any orientation change to reverseLandscape when Autorotation setting is off`() {
        setAutorotationEnabledValue(AUTOROTATION_OFF)

        testActivity.requestedOrientation = portraitOrientation

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenScreenAutoRotateSettingIsEnabled(
                ACCELEROMETER_LANDSCAPE_REVERSE_ORIENTATION
            )
        }
        verify(playerMVI, never()).submitAction(any<PlayerActionState.UpdatePlayerOrientationStateAction>())
        println("Current orientation = ${testActivity.requestedOrientation}")
        assertTrue(testActivity.requestedOrientation == portraitOrientation)
    }

    @Test
    fun `rotateWhenAutoRotationSettingIsDisabled calls orientation change to landscape when Autorotation setting is off`() {
        setAutorotationEnabledValue(AUTOROTATION_OFF)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenAutoRotationSettingIsDisabled(landscapeOrientation)
        }

        verify(playerMVI, atMost(1)).submitAction(fullScreenIntent)
        println("Current orientation = ${testActivity.requestedOrientation}")
        assertTrue(testActivity.requestedOrientation == landscapeOrientation)
    }

    @Test
    fun `rotateWhenAutoRotationSettingIsDisabled calls orientation change to portrait when Autorotation setting is off`() {
        setAutorotationEnabledValue(AUTOROTATION_OFF)

        playerOrientationHandler.rotateWhenAutoRotationSettingIsDisabled(portraitOrientation)

        verify(playerMVI).submitAction(portraitIntent)
        println("Current orientation = ${testActivity.requestedOrientation}")
        assertTrue(testActivity.requestedOrientation == unspecifiedOrientation)
    }

    @Test
    fun `rotateWhenAutoRotationSettingIsDisabled calls orientation change to reverse orientation in Autorotation setting is off`() {
        setAutorotationEnabledValue(AUTOROTATION_OFF)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenAutoRotationSettingIsDisabled(reverseLandscapeOrientation)
        }

        verify(playerMVI).submitAction(fullScreenIntent)
            println("Current orientation = ${testActivity.requestedOrientation}")
        assertTrue(testActivity.requestedOrientation == reverseLandscapeOrientation)
    }

    @Test
    fun `rotateWhenAutoRotationSettingIsDisabled does not call orientation change to landscape when Autorotation setting is on`() {
        setAutorotationEnabledValue(AUTOROTATION_ON)

        testActivity.requestedOrientation = portraitOrientation

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenAutoRotationSettingIsDisabled(landscapeOrientation)
        }

        verify(playerMVI, never()).submitAction(any<PlayerActionState.UpdatePlayerOrientationStateAction>())
        println("Current orientation = ${testActivity.requestedOrientation}")
        assertTrue(testActivity.requestedOrientation == portraitOrientation)

    }

    @Test
    fun `rotateWhenAutoRotationSettingIsDisabled does not call orientation change to portrait when Autorotation setting is on`() {
        setAutorotationEnabledValue(AUTOROTATION_ON)

        testActivity.requestedOrientation = portraitOrientation

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenAutoRotationSettingIsDisabled(portraitOrientation)
        }

        verify(playerMVI, never()).submitAction(any<PlayerActionState.UpdatePlayerOrientationStateAction>())
        println("Current orientation = ${testActivity.requestedOrientation}")
        assertTrue(testActivity.requestedOrientation == portraitOrientation)
    }

        @Test
        fun `rotateWhenAutoRotationSettingIsDisabled does not call orientation change to reverse orientation when Autorotation setting is on`() {
            setAutorotationEnabledValue(AUTOROTATION_ON)
            testActivity.requestedOrientation = portraitOrientation

            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                playerOrientationHandler.rotateWhenAutoRotationSettingIsDisabled(
                    reverseLandscapeOrientation
                )
            }

            verify(playerMVI,never()).submitAction(any<PlayerActionState.UpdatePlayerOrientationStateAction>())
            println("Current orientation = ${testActivity.requestedOrientation}")
            assertTrue(testActivity.requestedOrientation == portraitOrientation)
        }

    @Test
    fun `toggleFullScreen changes screen orientation to portrait orientation`() {
        setAutorotationEnabledValue(AUTOROTATION_ON)

        testActivity.requestedOrientation = landscapeOrientation
        // simulate the activity rotation listener on outer classes
        playerMVI.submitAction(fullScreenIntent)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            binding.fullScreenButton.performClick()
        }

        verify(playerMVI,atMost(1)).submitAction(portraitIntent)
        assertTrue(testActivity.requestedOrientation == portraitOrientation)
    }

    @Test
    fun `toggleFullScree changes screen orientation to landscape orientation`() {
        setAutorotationEnabledValue(AUTOROTATION_ON)

        // simulate the activity rotation listener on outer classes
        playerMVI.submitAction(portraitIntent)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            binding.fullScreenButton.performClick()
        }

        verify(playerMVI).submitAction(fullScreenIntent)
        assertTrue(testActivity.requestedOrientation == landscapeOrientation)
    }

    @Test
    fun `onFullScreen widget click from portrait enters fullScreen state and locks orientation for one rotation in Autorotation ON mode`() {
        setAutorotationEnabledValue(AUTOROTATION_ON)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            binding.fullScreenButton.performClick()
        }

        verify(playerMVI).submitAction(lockOrientationIntent)

        // simulate the activity rotation listener from outer classes
        playerOrientationHandler.rotateWhenScreenAutoRotateSettingIsEnabled(ACCELEROMETER_PORTRAIT_ORIENTATION)
        verify(playerMVI).submitAction(unlockOrientationIntent)
        verify(playerMVI, never()).submitAction(portraitIntent)
    }

    @Test
    fun `onFullScreen widget click exits from fullScreen state and locks orientation for one rotation in Autorotation ON mode`() {
        setAutorotationEnabledValue(AUTOROTATION_ON)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            // simulate the activity rotation listener from outer classes
            playerOrientationHandler.rotateWhenScreenAutoRotateSettingIsEnabled(ACCELEROMETER_LANDSCAPE_ORIENTATION)
            binding.fullScreenButton.performClick()
        }

        verify(playerMVI).submitAction(lockOrientationIntent)

        playerOrientationHandler.rotateWhenScreenAutoRotateSettingIsEnabled(ACCELEROMETER_LANDSCAPE_ORIENTATION)
        verify(playerMVI).submitAction(unlockOrientationIntent)

        // was called at first ACCELEROMETER_LANDSCAPE_ORIENTATION event
        verify(playerMVI, atMost(1)).submitAction(fullScreenIntent)
    }

    @Test
    fun `setUnspecifiedOrientationIfAutorotationOFF sets orientation to unspecified when Autorotation OFF after rotation to portrait`() {
        setAutorotationEnabledValue(AUTOROTATION_OFF)

        testActivity.requestedOrientation = portraitOrientation

        playerMVI.submitAction(unlockOrientationIntent)
        playerOrientationHandler.rotateWhenAutoRotationSettingIsDisabled(portraitOrientation)

        assertTrue(testActivity.requestedOrientation == unspecifiedOrientation)
    }

    @Test
    fun `setUnspecifiedOrientationIfAutorotationOFF does not set orientation to unspecified when Autorotation is OFF after rotation to landscape`() {
        setAutorotationEnabledValue(AUTOROTATION_OFF)

        playerMVI.submitAction(unlockOrientationIntent)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenAutoRotationSettingIsDisabled(landscapeOrientation)
        }

        assertTrue(testActivity.requestedOrientation != unspecifiedOrientation)
        assertTrue(testActivity.requestedOrientation == landscapeOrientation)
    }

    @Test
    fun `setUnspecifiedOrientationIfAutorotationOFF does not set orientation to unspecified when Autorotation is OFF after rotation to reverseLandscape`() {
        setAutorotationEnabledValue(AUTOROTATION_OFF)

        playerMVI.submitAction(unlockOrientationIntent)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenAutoRotationSettingIsDisabled(reverseLandscapeOrientation)
        }

        assertTrue(testActivity.requestedOrientation != unspecifiedOrientation)
        assertTrue(testActivity.requestedOrientation == reverseLandscapeOrientation)
    }
}
