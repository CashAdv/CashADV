package app.cashadvisor.authorization.presentation.ui.models

sealed interface StartScreenSideEffects {
    data object NavigateToPinCodeScreen : StartScreenSideEffects
    data object NavigateToAuthenticationScreen : StartScreenSideEffects
    data object NavigateToUiKitScreen : StartScreenSideEffects
    data class ShowToast(val message: Int) : StartScreenSideEffects
}