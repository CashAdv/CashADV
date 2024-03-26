package app.cashadvisor.signup.presentation.viewmodel.models

sealed interface SignupSideEffect{
    data class ShowMessage(val message: String): SignupSideEffect
}