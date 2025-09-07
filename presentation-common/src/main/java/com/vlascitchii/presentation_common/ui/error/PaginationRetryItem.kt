package com.vlascitchii.presentation_common.ui.error

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.vlascitchii.presentation_common.R
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme

@Composable
fun PaginationRetryItem(modifier: Modifier = Modifier, onRetryClick: () -> Unit = {}) {
    val retryItemDescription = stringResource(R.string.paging_error_retry_item)

    Card(
        modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(R.dimen.padding_small_8),
                end = dimensionResource(R.dimen.padding_small_8),
                bottom = dimensionResource(R.dimen.padding_medium_16)
            )
            .height(dimensionResource(R.dimen.height_medium_48))
            .clickable(
                onClickLabel = retryItemDescription,
                onClick = onRetryClick
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.elevation_medium_16)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.shape_small_8))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Refresh list button",
            )
        }
    }
}

@PreviewLightDark
@Composable
fun PaginationRetryItemPreview() {
    BlueTubeComposeTheme {
        Surface {
            PaginationRetryItem(Modifier) {}
        }
    }
}
