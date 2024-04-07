package app.cashadvisor.authorization.presentation.ui.models

sealed interface LoginScreenSideEffects{
    data object NoInternetConnection: LoginScreenSideEffects
    data object LoginSuccessfullyConfirmed: LoginScreenSideEffects
    data object HideKeyboard: LoginScreenSideEffects
    data class FailedToConfirmLogin(val lockDuration: String): LoginScreenSideEffects
}