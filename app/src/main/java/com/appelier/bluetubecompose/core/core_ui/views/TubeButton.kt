package com.appelier.bluetubecompose.core.core_ui.views

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.appelier.bluetubecompose.core.core_ui.theme.BlueTubeComposeTheme

@Composable
fun TubeButton(onClickAction: () -> Unit) {
    Button(
        shape = MaterialTheme.shapes.small,
        onClick = { onClickAction }
    ) {
        Text(text = "Test button")
    }
}

@Preview
@Composable
private fun BlueTubeButtonPreview() {
    BlueTubeComposeTheme {
        TubeButton {}
    }
}
