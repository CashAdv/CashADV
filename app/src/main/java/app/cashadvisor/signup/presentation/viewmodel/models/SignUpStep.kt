package app.cashadvisor.signup.presentation.viewmodel.models

sealed interface SignUpStep {

    data object SignupScreen: SignUpStep

    data class ConfirmationCodeScreen(val resendingCoolDownSec: String? = null): SignUpStep

    data object SignupEmailSuccessfullyConfirmed: SignUpStep
}