package app.cashadvisor.authorization.presentation.ui.test

import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.Password

data class TestStartState(
    val email: Email = Email(""),
    val password: Password = Password("password"),
    val emailCode: ConfirmCode = ConfirmCode(""),
    val loginCode: ConfirmCode = ConfirmCode(""),
    val isEmailValid: Boolean = false,
    val isEmailCodeValid: Boolean = false,
    val isLoginCodeValid: Boolean = false,
    val isRegisterInProgress: Boolean = false,
    val isLoginInProgress: Boolean = false
)