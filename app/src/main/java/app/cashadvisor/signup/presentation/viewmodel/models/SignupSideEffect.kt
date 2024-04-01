package app.cashadvisor.signup.presentation.viewmodel.models

sealed interface SignupSideEffect{
    data class ShowMessage(val messageId: Int): SignupSideEffect

    data class ShowСhangeableMessage(val messageId: Int, val messageСhangeable: String): SignupSideEffect
}