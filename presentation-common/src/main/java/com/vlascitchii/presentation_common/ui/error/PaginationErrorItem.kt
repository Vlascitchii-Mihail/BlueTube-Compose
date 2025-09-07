package com.vlascitchii.presentation_common.ui.error

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlascitchii.presentation_common.R
import com.vlascitchii.presentation_common.ui.BlueTubeButton

@Composable
fun PaginationErrorItem(
    errorText: String?,
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit
) {
    val paginationError = stringResource(R.string.paging_error_msg_txt)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = errorText ?: paginationError,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .semantics { contentDescription = paginationError },
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge
        )
        BlueTubeButton(
            text = stringResource(id = R.string.paging_error_retry_btn),
            onClickAction = onRetryClick
        )
    }
}

@Preview
@Composable
fun PaginationErrorItemPreview() {
    PaginationErrorItem("Error") {}
}