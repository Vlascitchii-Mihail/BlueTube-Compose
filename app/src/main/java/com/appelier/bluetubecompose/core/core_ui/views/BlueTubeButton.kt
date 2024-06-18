package com.appelier.bluetubecompose.core.core_ui.views

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.appelier.bluetubecompose.core.core_ui.theme.BlueTubeComposeTheme

@Composable
fun BlueTubeButton(text: String = "Test button", onClickAction: () -> Unit) {
    Button(
        shape = MaterialTheme.shapes.small,
        onClick = onClickAction
    ) {
        Text(text = text)
    }
}

@Preview
@Composable
private fun BlueTubeButtonPreview() {
    BlueTubeComposeTheme {
        BlueTubeButton {}
    }
}
