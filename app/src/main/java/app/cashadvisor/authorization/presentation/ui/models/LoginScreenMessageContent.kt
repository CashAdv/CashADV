package app.cashadvisor.authorization.presentation.ui.models

sealed interface LoginScreenMessageContent{
    data object EmailFormatError: LoginScreenMessageContent
    data object PasswordFormatError: LoginScreenMessageContent
    data object PasswordCountError: LoginScreenMessageContent
    data object EmailAndPasswordFormatErrors: LoginScreenMessageContent
    data object EmailFormatAndPasswordCountErrors: LoginScreenMessageContent
    data class LoginError(val message: String): LoginScreenMessageContent
    data class ConfirmationCodeMessage(val message: String): LoginScreenMessageContent
}