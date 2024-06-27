package app.cashadvisor.authorization.domain.models.states

import app.cashadvisor.authorization.presentation.viewmodel.models.RecoveryEmailValidationState
import app.cashadvisor.authorization.presentation.viewmodel.models.RecoveryPasswordValidationState

sealed interface PasswordRecoveryScreenState{
    data class EmailInput(
        val emailState: RecoveryEmailValidationState? = null,
        val isLoginSuccessful: Boolean? = null,
        val isBtnLoginEnabled: Boolean,
        val isLoading: Boolean? = null
    ): PasswordRecoveryScreenState
    data class ConfirmationCode(val resendingCoolDownSec: String? = null) :
        PasswordRecoveryScreenState

    data class PasswordInput(
        val passwordState: RecoveryPasswordValidationState? = null,
        val resetPasswordSuccessful:Boolean? = null,
        val isBtnResetPasswordEnabled: Boolean,
        val isLoading: Boolean? = null
    ):PasswordRecoveryScreenState
}



