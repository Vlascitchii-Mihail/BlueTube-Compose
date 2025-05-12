package com.vlascitchii.presentation_common.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme

@Composable
fun TextBodyLargeCentered(text: String, modifier: Modifier, color: Color = MaterialTheme.colorScheme.onBackground) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        modifier = modifier,
        color = color,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Preview
@Composable
private fun BodyTextPreview() {
    BlueTubeComposeTheme {
        TextBodyLargeCentered(text = "Hello world", Modifier, color = MaterialTheme.colorScheme.onPrimary)
    }
}
