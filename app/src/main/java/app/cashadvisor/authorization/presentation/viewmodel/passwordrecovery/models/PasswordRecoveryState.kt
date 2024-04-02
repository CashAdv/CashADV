package app.cashadvisor.authorization.presentation.viewmodel.passwordrecovery.models

import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.Password

data class PasswordRecoveryState(
    val email: Email = Email(""),
    val emailCode: ConfirmCode = ConfirmCode(""),
    val password: Password = Password("password"),
    val recoveryCode: ConfirmCode = ConfirmCode(""),
    val isEmailValid: Boolean = false,
    val isEmailCodeValid: Boolean = false,
)
