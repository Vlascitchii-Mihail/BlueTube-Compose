package com.appelier.bluetubecompose.core.core_ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PaginationErrorItem(
    errorText: String? = "Videos not found",
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = errorText ?: "Videos not found",
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth().padding(8.dp),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge
        )
        BlueTubeButton(text = "Try Again", onClickAction = onRetryClick)
    }
}

@Preview
@Composable
fun PaginationErrorItemPreview() {
    PaginationErrorItem("Error") {}
}