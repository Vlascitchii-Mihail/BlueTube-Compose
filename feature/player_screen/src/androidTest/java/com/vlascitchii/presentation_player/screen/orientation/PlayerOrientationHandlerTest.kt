package com.vlascitchii.presentation_player.screen.orientation

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import androidx.paging.PagingData
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vlascitchii.domain.usecase.UseCase
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.player_screen.databinding.FragmentPlayVideoBinding
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityAbstraction
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.ui.state_common.UiSingleEvent
import com.vlascitchii.presentation_player.screen.OrientationState
import com.vlascitchii.presentation_player.screen.TestActivity
import com.vlascitchii.presentation_player.screen.screen.PlayerMviHandler
import com.vlascitchii.presentation_player.screen.screen.VideoPlayerViewModel
import com.vlascitchii.presentation_player.screen.state.PlayerActionState
import com.vlascitchii.ui_test.autorotation_system_setting.AUTOROTATION_OFF
import com.vlascitchii.ui_test.autorotation_system_setting.AUTOROTATION_ON
import com.vlascitchii.ui_test.autorotation_system_setting.landscapeOrientation
import com.vlascitchii.ui_test.autorotation_system_setting.portraitOrientation
import com.vlascitchii.ui_test.autorotation_system_setting.reverseLandscapeOrientation
import com.vlascitchii.ui_test.autorotation_system_setting.setAutorotationEnabledValue
import com.vlascitchii.ui_test.autorotation_system_setting.unspecifiedOrientation
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.atMost
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify

@RunWith(AndroidJUnit4::class)
class PlayerOrientationHandlerTest {

    private lateinit var scenario: ActivityScenario<TestActivity>
    private lateinit var playerOrientationHandler: PlayerOrientationHandler
    private lateinit var testActivity: Activity
    private lateinit var binding: FragmentPlayVideoBinding

    private val videoPlayerUseCase: UseCase<VideoPlayerUseCase.PlayerRequest, VideoPlayerUseCase.PlayerResponse> = mock()
    private val videoPlayerConverter: CommonResultConverter<VideoPlayerUseCase.PlayerResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>> = mock()

    private val context: Context = ApplicationProvider.getApplicationContext<Context>()
    private val networkConnectivityObserver: NetworkConnectivityAbstraction = NetworkConnectivityObserver(
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    )

    private val videoPlayerViewModel: VideoPlayerViewModel = VideoPlayerViewModel(
        videoPlayerUseCase,
        videoPlayerConverter,
        networkConnectivityObserver,
        Dispatchers.IO
    )
    private val mviHandler: PlayerMviHandler = spy(
        PlayerMviHandler(
            videoPlayerViewModel,
            navigationHandler = { event: UiSingleEvent -> Unit }
        )
    )

    private val fullScreenIntent = PlayerActionState.UpdatePlayerOrientationStateAction(OrientationState.FULL_SCREEN)
    private val portraitIntent = PlayerActionState.UpdatePlayerOrientationStateAction(OrientationState.PORTRAIT)
    private val lockOrientationIntent = PlayerActionState.ApproveOrientationChange(false)
    private val unlockOrientationIntent = PlayerActionState.ApproveOrientationChange(true)

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(TestActivity::class.java)
            .onActivity { activity: Activity ->
                testActivity = activity
                binding = FragmentPlayVideoBinding.inflate(activity.layoutInflater)

                playerOrientationHandler = PlayerOrientationHandler(
                    binding = binding,
                    activity = activity,
                    mviHandler = mviHandler,
                    playerStateFlow = videoPlayerViewModel.playerStateFlow,
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

        verify(mviHandler).submitAction(portraitIntent)
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

        verify(mviHandler).submitAction(fullScreenIntent)
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

        verify(mviHandler).submitAction(fullScreenIntent)
        assertTrue(testActivity.requestedOrientation == reverseLandscapeOrientation)
    }

    @Test
    fun `outFromFullScreenAndLockPortraitOrientation changes to portrait orientation`() {
        setAutorotationEnabledValue(AUTOROTATION_ON)

        testActivity.requestedOrientation = landscapeOrientation
        playerOrientationHandler.outFromFullScreenSetStaticPortraitOrientation()

        verify(mviHandler).submitAction(portraitIntent)
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

        verify(mviHandler).submitAction(fullScreenIntent)
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

        verify(mviHandler).submitAction(portraitIntent)
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

        verify(mviHandler).submitAction(fullScreenIntent)
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

        verify(mviHandler, never()).submitAction(any<PlayerActionState.UpdatePlayerOrientationStateAction>())
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

        verify(mviHandler, never()).submitAction(any<PlayerActionState.UpdatePlayerOrientationStateAction>())
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
        verify(mviHandler, never()).submitAction(any<PlayerActionState.UpdatePlayerOrientationStateAction>())
        println("Current orientation = ${testActivity.requestedOrientation}")
        assertTrue(testActivity.requestedOrientation == portraitOrientation)
    }

    @Test
    fun `rotateWhenAutoRotationSettingIsDisabled calls orientation change to landscape when Autorotation setting is off`() {
        setAutorotationEnabledValue(AUTOROTATION_OFF)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenAutoRotationSettingIsDisabled(landscapeOrientation)
        }

        verify(mviHandler, atMost(1)).submitAction(fullScreenIntent)
        println("Current orientation = ${testActivity.requestedOrientation}")
        assertTrue(testActivity.requestedOrientation == landscapeOrientation)
    }

    @Test
    fun `rotateWhenAutoRotationSettingIsDisabled calls orientation change to portrait when Autorotation setting is off`() {
        setAutorotationEnabledValue(AUTOROTATION_OFF)

        playerOrientationHandler.rotateWhenAutoRotationSettingIsDisabled(portraitOrientation)

        verify(mviHandler).submitAction(portraitIntent)
        println("Current orientation = ${testActivity.requestedOrientation}")
        assertTrue(testActivity.requestedOrientation == unspecifiedOrientation)
    }

    @Test
    fun `rotateWhenAutoRotationSettingIsDisabled calls orientation change to reverse orientation in Autorotation setting is off`() {
        setAutorotationEnabledValue(AUTOROTATION_OFF)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenAutoRotationSettingIsDisabled(reverseLandscapeOrientation)
        }

        verify(mviHandler).submitAction(fullScreenIntent)
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

        verify(mviHandler, never()).submitAction(any<PlayerActionState.UpdatePlayerOrientationStateAction>())
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

        verify(mviHandler, never()).submitAction(any<PlayerActionState.UpdatePlayerOrientationStateAction>())
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

            verify(mviHandler,never()).submitAction(any<PlayerActionState.UpdatePlayerOrientationStateAction>())
            println("Current orientation = ${testActivity.requestedOrientation}")
            assertTrue(testActivity.requestedOrientation == portraitOrientation)
        }

    @Test
    fun `toggleFullScreen changes screen orientation to portrait orientation`() {
        setAutorotationEnabledValue(AUTOROTATION_ON)

        testActivity.requestedOrientation = landscapeOrientation
        // simulate the activity rotation listener on outer classes
        mviHandler.submitAction(fullScreenIntent)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            binding.fullScreenButton.performClick()
        }

        verify(mviHandler,atMost(1)).submitAction(portraitIntent)
        assertTrue(testActivity.requestedOrientation == portraitOrientation)
    }

    @Test
    fun `toggleFullScree changes screen orientation to landscape orientation`() {
        setAutorotationEnabledValue(AUTOROTATION_ON)

        // simulate the activity rotation listener on outer classes
        mviHandler.submitAction(portraitIntent)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            binding.fullScreenButton.performClick()
        }

        verify(mviHandler).submitAction(fullScreenIntent)
        assertTrue(testActivity.requestedOrientation == landscapeOrientation)
    }

    @Test
    fun `onFullScreen widget click from portrait enters fullScreen state and locks orientation for one rotation in Autorotation ON mode`() {
        setAutorotationEnabledValue(AUTOROTATION_ON)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            binding.fullScreenButton.performClick()
        }

        verify(mviHandler).submitAction(lockOrientationIntent)

        // simulate the activity rotation listener from outer classes
        playerOrientationHandler.rotateWhenScreenAutoRotateSettingIsEnabled(ACCELEROMETER_PORTRAIT_ORIENTATION)
        verify(mviHandler).submitAction(unlockOrientationIntent)
        verify(mviHandler, never()).submitAction(portraitIntent)
    }

    @Test
    fun `onFullScreen widget click exits from fullScreen state and locks orientation for one rotation in Autorotation ON mode`() {
        setAutorotationEnabledValue(AUTOROTATION_ON)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            // simulate the activity rotation listener from outer classes
            playerOrientationHandler.rotateWhenScreenAutoRotateSettingIsEnabled(ACCELEROMETER_LANDSCAPE_ORIENTATION)
            binding.fullScreenButton.performClick()
        }

        verify(mviHandler).submitAction(lockOrientationIntent)

        playerOrientationHandler.rotateWhenScreenAutoRotateSettingIsEnabled(ACCELEROMETER_LANDSCAPE_ORIENTATION)
        verify(mviHandler).submitAction(unlockOrientationIntent)

        // was called at first ACCELEROMETER_LANDSCAPE_ORIENTATION event
        verify(mviHandler, atMost(1)).submitAction(fullScreenIntent)
    }

    @Test
    fun `setUnspecifiedOrientationIfAutorotationOFF sets orientation to unspecified when Autorotation OFF after rotation to portrait`() {
        setAutorotationEnabledValue(AUTOROTATION_OFF)

        testActivity.requestedOrientation = portraitOrientation

        mviHandler.submitAction(unlockOrientationIntent)
        playerOrientationHandler.rotateWhenAutoRotationSettingIsDisabled(portraitOrientation)

        assertTrue(testActivity.requestedOrientation == unspecifiedOrientation)
    }

    @Test
    fun `setUnspecifiedOrientationIfAutorotationOFF does not set orientation to unspecified when Autorotation is OFF after rotation to landscape`() {
        setAutorotationEnabledValue(AUTOROTATION_OFF)

        mviHandler.submitAction(unlockOrientationIntent)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenAutoRotationSettingIsDisabled(landscapeOrientation)
        }

        assertTrue(testActivity.requestedOrientation != unspecifiedOrientation)
        assertTrue(testActivity.requestedOrientation == landscapeOrientation)
    }

    @Test
    fun `setUnspecifiedOrientationIfAutorotationOFF does not set orientation to unspecified when Autorotation is OFF after rotation to reverseLandscape`() {
        setAutorotationEnabledValue(AUTOROTATION_OFF)

        mviHandler.submitAction(unlockOrientationIntent)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            playerOrientationHandler.rotateWhenAutoRotationSettingIsDisabled(reverseLandscapeOrientation)
        }

        assertTrue(testActivity.requestedOrientation != unspecifiedOrientation)
        assertTrue(testActivity.requestedOrientation == reverseLandscapeOrientation)
    }
}
