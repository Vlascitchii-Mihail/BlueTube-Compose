package com.vlascitchii.presenttion_settings.screen_settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.vlascitchii.presentation_common.ui.TextBodyLargeCentered
import com.vlascitchii.presenttion_settings.R

const val SETTINGS_BOTTOM_NAV_NAME: String = "Settings"

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    bottomNavigation: @Composable () -> Unit
) {
    val settingsScreenDescription = stringResource(id = R.string.settings_screen_description)

    Scaffold(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background).semantics { contentDescription = settingsScreenDescription },
        content = { paddingValue ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValue)
                    .semantics { contentDescription = settingsScreenDescription }
            ) {
                TextBodyLargeCentered(
                    text = settingsScreenDescription,
                    modifier = modifier.semantics { contentDescription = settingsScreenDescription }
                )
            }
        },
        bottomBar = {
            bottomNavigation.invoke()
        }
    )
}
