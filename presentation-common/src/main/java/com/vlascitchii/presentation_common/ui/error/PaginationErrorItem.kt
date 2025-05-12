package com.vlascitchii.presentation_common.ui.error

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlascitchii.presentation_common.R
import com.vlascitchii.presentation_common.ui.BlueTubeButton
import com.vlascitchii.presentation_common.utils.Core.PAGING_ERROR_MSG
import com.vlascitchii.presentation_common.utils.VideoListScreenTags.VIDEO_LIST_ERROR

@Composable
fun PaginationErrorItem(
    errorText: String?,
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.testTag(VIDEO_LIST_ERROR)) {
        Text(
            text = errorText ?: stringResource(id = R.string.paging_error_msg_txt),
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag(PAGING_ERROR_MSG),
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