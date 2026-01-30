package com.vlascitchii.presentation_common.ui.error

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.vlascitchii.common_ui.R
import com.vlascitchii.presentation_common.ui.BlueTubeButton
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme

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
                .padding(dimensionResource(R.dimen.padding_small_8))
                .semantics { contentDescription = paginationError },
            style = MaterialTheme.typography.bodyLarge
        )
        BlueTubeButton(
            text = stringResource(id = R.string.paging_error_retry_btn),
            onClickAction = onRetryClick
        )
    }
}

@PreviewLightDark
@Composable
fun PaginationErrorItemPreview() {
    BlueTubeComposeTheme {
        Surface {
            PaginationErrorItem("Videos not found") {}
        }
    }
}
