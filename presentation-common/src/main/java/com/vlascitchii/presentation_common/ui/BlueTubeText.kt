package com.vlascitchii.presentation_common.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
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

@PreviewLightDark
@Composable
private fun BodyTextPreview() {
    BlueTubeComposeTheme {
        Surface {
            TextBodyLargeCentered(text = "Hello world", Modifier)
        }
    }
}
