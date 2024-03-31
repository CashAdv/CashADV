package app.cashadvisor.signup.presentation.viewmodel.models

sealed interface SignupScreenState {

    data object SignupScreen: SignupScreenState

    data class ConfirmationCodeScreen(val resendingCoolDownSec: String? = null): SignupScreenState

    data object SignupEmailSuccessfullyConfirmed: SignupScreenState
}