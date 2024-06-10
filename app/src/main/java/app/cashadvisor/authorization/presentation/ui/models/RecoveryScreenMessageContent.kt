package app.cashadvisor.authorization.presentation.ui.models

sealed interface RecoveryScreenMessageContent {

    data object EmailFormatError: RecoveryScreenMessageContent
    data object PasswordFormatError: RecoveryScreenMessageContent
    data object PasswordCountError: RecoveryScreenMessageContent
    data class LoginError(val message: String): RecoveryScreenMessageContent
    data class ResetPasswordError(val message: String):RecoveryScreenMessageContent
    data class ConfirmationCodeMessage(val message: String): RecoveryScreenMessageContent
}