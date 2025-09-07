package com.vlascitchii.presentation_common.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.vlascitchii.presentation_common.R
import com.vlascitchii.presentation_common.ui.state.UiState

@Composable
fun <T : Any> CommonScreen(
    state: UiState<T>,
    onSuccess: @Composable (T) -> Unit
) {
    when(state) {
        is UiState.Loading -> Loading()
        is UiState.Error -> Error(state.errorMessage)
        is UiState.Success -> onSuccess(state.data)
    }
}

@Composable
fun Loading() {
    val loadingDescription = stringResource(R.string.common_loading_compose_desc)

    Column(
        modifier = Modifier.fillMaxSize().semantics { contentDescription = loadingDescription },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun Error(errorMsg: String) {
    val errorDescription = stringResource(R.string.common_error_compos_desc)
    Column(modifier = Modifier.fillMaxSize().semantics { contentDescription = errorDescription },
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Snackbar{ Text(text = errorMsg) }
    }
}

@Preview
@Composable
fun PreviewLoading() {
    Loading()
}

@Preview
@Composable
fun PreviewError() {
    Error("Test error")
}