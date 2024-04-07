package app.cashadvisor.authorization.presentation.viewmodel.passwordrecovery

sealed interface RecoverySideEffect {
    data class ShowMessage(val message:String):RecoverySideEffect
}