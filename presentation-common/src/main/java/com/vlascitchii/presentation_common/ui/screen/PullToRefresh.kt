package com.vlascitchii.presentation_common.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val LOAD_DELAY = 1500L
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefresh(
    scrollableView: @Composable () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {

    val rememberPullToRefreshState = rememberPullToRefreshState()
    var refreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    PullToRefreshBox(
        state = rememberPullToRefreshState,
        isRefreshing = refreshing,
        onRefresh = {
            coroutineScope.launch {
                refreshing = true
                delay(LOAD_DELAY)
                onRefresh.invoke()
                refreshing = false
            }
        },
        modifier = modifier
    ) {
        scrollableView.invoke()
    }
}
