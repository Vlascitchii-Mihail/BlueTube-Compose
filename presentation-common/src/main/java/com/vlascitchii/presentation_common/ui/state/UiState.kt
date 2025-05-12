package com.vlascitchii.presentation_common.ui.state

sealed class UiState<out T: Any> {

    object Loading : UiState<Nothing>()
    data class Error<T: Any>(val errorMessage: String) : UiState<T>()
    data class Success<T: Any>(val data: T) : UiState<T>()
}