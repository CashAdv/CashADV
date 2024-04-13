package app.cashadvisor.authorization.presentation.ui.models

sealed interface RecoverySideEffect {
    data class ShowMessage(val message:String): RecoverySideEffect
}