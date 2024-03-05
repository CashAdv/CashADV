package app.cashadvisor.authorization.presentation.ui.models

data class StartScreenUiState(
    val state: UiState
) {
    sealed interface UiState {
        data object Loading : UiState
        data class Success(val isDebug: Boolean = false) : UiState
        data object Error : UiState
    }

}