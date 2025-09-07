@file:OptIn(ExperimentalMaterial3Api::class)

package com.vlascitchii.presentation_video_list.screen.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import com.vlascitchii.presentation_video_list.screen.state.SearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.vlascitchii.presentation_common.R as RCommon
import com.vlascitchii.presentation_video_list.R as RPresentationList

private const val SEARCH_INPUT_DELAY = 2000L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBarBlueTube(
    searchText: StateFlow<TextFieldValue>,
    onTextChange: (String) -> Unit,
    updateSearchState: (SearchState) -> Unit,
    onSearchClicked: () -> Unit,
    scrollAppBarBehaviour: TopAppBarScrollBehavior,
    modifier: Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val inputScope = rememberCoroutineScope()
    var inputSearchJob by remember { mutableStateOf<Job?>(null) }
    val searchQuery by searchText.collectAsStateWithLifecycle()
    var previousQuery: String = remember { "" }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    fun performSearch(input: String) {
        if (input != "" && input != previousQuery) {
            inputSearchJob?.cancel()
            inputSearchJob = inputScope.launch {
                delay(SEARCH_INPUT_DELAY)
                onSearchClicked.invoke()
                previousQuery = input
            }
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        shadowElevation = dimensionResource(RCommon.dimen.elevation_medium_16),
        tonalElevation = dimensionResource(RCommon.dimen.elevation_medium_16)

    ) {
        TopAppBar(
            navigationIcon = {
                IconButton(
                    modifier = modifier.alpha(0.5f),
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = RPresentationList.string.appbar_search_icon_descr),
                    )
                }
            },
            title = {
                OutlinedTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = searchQuery,
                    onValueChange = { input ->
                        onTextChange.invoke(input.text)
                        performSearch(input.text)
                    },
                    placeholder = {
                        Text(
                            modifier = modifier.alpha(0.5f),
                            text = stringResource(id = RPresentationList.string.search_placeholder),
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (searchQuery.text.isNotEmpty()) onTextChange("")
                                else updateSearchState.invoke(SearchState.CLOSED)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(id = RPresentationList.string.appbar_close_icon_descr),
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            performSearch(searchQuery.text)
                            keyboardController?.hide()
                        }
                    )
                )
            },
            scrollBehavior = scrollAppBarBehaviour
        )
    }
}

@PreviewLightDark
@Composable
fun PreviewSearchAppBarBlueTube() {
    val searchText = remember { MutableStateFlow(TextFieldValue("")) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    BlueTubeComposeTheme {
        Surface {
            SearchAppBarBlueTube(
                searchText = searchText,
                onTextChange = {},
                updateSearchState = {},
                onSearchClicked = {},
                scrollAppBarBehaviour = scrollBehavior,
                modifier = Modifier,
            )
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewSearchAppBarBlueTubeWithText() {
    val searchText = remember { MutableStateFlow(TextFieldValue("Test text")) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    BlueTubeComposeTheme {
        Surface {
            SearchAppBarBlueTube(
                searchText = searchText,
                onTextChange = {},
                updateSearchState = {},
                onSearchClicked = {},
                scrollAppBarBehaviour = scrollBehavior,
                modifier = Modifier,
            )
        }
    }
}
