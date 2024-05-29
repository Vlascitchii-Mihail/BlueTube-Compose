package com.appelier.bluetubecompose.core.core_ui.views

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.appelier.bluetubecompose.core.core_ui.theme.BlueTubeComposeTheme

@Composable
fun BodyText(text: String, color: Color = Color.White) {
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Preview
@Composable
private fun BodyTextPreview() {
    BlueTubeComposeTheme {
        BodyText(text = "Hello world", color = MaterialTheme.colorScheme.onPrimary)
    }
}
