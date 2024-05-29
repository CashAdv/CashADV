package app.cashadvisor.authorization.presentation.ui.models

sealed interface RecoverySideEffect {
    data object NoInternetConnection: RecoverySideEffect
    data object PasswordSuccessfullyConfirmed: RecoverySideEffect
    data object HideKeyboard: RecoverySideEffect
    data object ClearConfirmationCode:RecoverySideEffect


}