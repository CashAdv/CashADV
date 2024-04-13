package app.cashadvisor.authorization.presentation.viewmodel.models

sealed interface RecoveryPasswordScreenEvent {
    class SetEmail(val email: String) : RecoveryPasswordScreenEvent
    data object Recovery: RecoveryPasswordScreenEvent
    class SetEmailConfirmCode(val code:String) : RecoveryPasswordScreenEvent
    data object ConfirmEmail: RecoveryPasswordScreenEvent
    class SetPassword(val password:String): RecoveryPasswordScreenEvent
    data object ConfirmNewPassword: RecoveryPasswordScreenEvent
}