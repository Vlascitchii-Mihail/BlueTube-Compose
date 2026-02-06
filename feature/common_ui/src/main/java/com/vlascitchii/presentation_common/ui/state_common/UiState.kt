package com.vlascitchii.presentation_common.ui.state_common

sealed class UiState<out RESULT: Any> {

    object Loading : UiState<Nothing>()
    data class Error<RESULT: Any>(val errorMessage: String) : UiState<RESULT>()
    data class Success<RESULT: Any>(val data: RESULT) : UiState<RESULT>()
}
