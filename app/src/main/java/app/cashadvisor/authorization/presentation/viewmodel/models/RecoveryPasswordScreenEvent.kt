package app.cashadvisor.authorization.presentation.viewmodel.models

import android.content.Context

sealed interface RecoveryPasswordScreenEvent {
    class SetEmail(val email: String) : RecoveryPasswordScreenEvent
    data object Recovery: RecoveryPasswordScreenEvent
    class SetEmailConfirmCode(val code:String, val context: Context) : RecoveryPasswordScreenEvent
    class ConfirmEmail(val context: Context): RecoveryPasswordScreenEvent
    class SetPassword(val password:String, val context: Context): RecoveryPasswordScreenEvent
    class ConfirmNewPassword(val context: Context): RecoveryPasswordScreenEvent
}