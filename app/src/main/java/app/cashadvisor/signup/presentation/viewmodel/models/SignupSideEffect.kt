package app.cashadvisor.signup.presentation.viewmodel.models

sealed interface SignupSideEffect{
    data class ShowMessage(val messageId: Int): SignupSideEffect

    data class ShowChangeableMessage(val messageId: Int, val messageChangeable: String): SignupSideEffect

    data object NoInternetConnection: SignupSideEffect
}