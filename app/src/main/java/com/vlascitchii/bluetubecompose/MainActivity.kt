@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.vlascitchii.bluetubecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import com.vlascitchii.presentation_common.ui.screen.LocalWindowSizeClass
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            CompositionLocalProvider(
                LocalWindowSizeClass provides calculateWindowSizeClass(activity = this@MainActivity),
            ) {
                BlueTubeApp()
            }
        }
    }
}
