package com.vlascitchii.presenttion_settings.screen_settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.vlascitchii.presentation_common.ui.TextBodyLargeCentered
import com.vlascitchii.presentation_common.utils.NavigationTags.SETTINGS_SCREEN
import com.vlascitchii.presenttion_settings.R

@Composable
fun SettingsScreen() {

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .testTag(SETTINGS_SCREEN),
        content = { paddingValue ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValue)) {
                TextBodyLargeCentered(
                    text = stringResource(id = R.string.settings_screen),
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
    )
}