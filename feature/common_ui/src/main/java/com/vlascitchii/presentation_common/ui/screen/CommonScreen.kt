@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.vlascitchii.presentation_common.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.vlascitchii.common_ui.R
import com.vlascitchii.presentation_common.ui.state_common.UiState
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme

internal val ASPECT_RATIO_16_BY_9 = 1.7.dp

val LocalWindowSizeClass =
    compositionLocalOf { WindowSizeClass.calculateFromSize(DpSize.Zero) }

fun previewWindowSizeClass(dpSize: DpSize): WindowSizeClass =
    WindowSizeClass.calculateFromSize(dpSize)

@Composable
fun <T : Any> CommonScreen(
    state: UiState<T>,
    onSuccess: @Composable (T) -> Unit
) {
    when (state) {
        is UiState.Loading -> Loading()
        is UiState.Error -> {
            Error(state.errorMessage)
        }
        is UiState.Success -> {
            onSuccess(state.data)
        }
    }
}

@Composable
fun Loading() {
    val commonLoadingScreenDescription = stringResource(R.string.common_loading_screen_compose_description)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .semantics { contentDescription = commonLoadingScreenDescription },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun Error(errorMsg: String) {
    val errorDescription = stringResource(R.string.common_error_compos_desc)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .semantics { contentDescription = errorDescription },
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Snackbar { Text(text = errorMsg) }
    }
}

@PreviewLightDark
@Composable
fun PreviewLoading() {
    BlueTubeComposeTheme {
        Surface { Loading() }
    }
}

@PreviewLightDark
@Composable
fun PreviewError() {
    BlueTubeComposeTheme {
        Surface {
            Error("Test error")
        }
    }
}
