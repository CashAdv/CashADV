package app.cashadvisor.authorization.presentation.viewmodel.models

sealed interface LoginScreenState {
    data class CredentialsInput(
        val emailState: LoginEmailValidationState? = null,
        val passwordState: LoginPasswordValidationState? = null,
        val isLoginSuccessful: Boolean? = null,
        val isBtnLoginEnabled: Boolean,
        val isLoading: Boolean? = null
    ) : LoginScreenState

    data class ConfirmationCode(val resendingCoolDownSec: String? = null) : LoginScreenState
}