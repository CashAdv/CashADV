package app.cashadvisor.authorization.domain.models.states

sealed interface PasswordRecoveryScreenState{
    data class EmailInput(
        val emailState: EmailValidationState? = null,
        val isLoginSuccessful: Boolean? = null,
        val isBtnLoginEnabled: Boolean,
        val isLoading: Boolean? = null
    ): PasswordRecoveryScreenState
    data class ConfirmationCode(val resendingCoolDownSec: String? = null) :
        PasswordRecoveryScreenState

    data class PasswordInput(
        val passwordState: PasswordValidationState? = null,
        val resetPasswordSuccessful:Boolean? = null,
        val isBtnResetPasswordEnabled: Boolean,
        val isLoading: Boolean? = null
    ):PasswordRecoveryScreenState
}



