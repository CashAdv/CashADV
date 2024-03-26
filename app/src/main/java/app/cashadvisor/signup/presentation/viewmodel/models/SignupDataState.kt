package app.cashadvisor.signup.presentation.viewmodel.models

import app.cashadvisor.authorization.domain.models.ConfirmCode
import app.cashadvisor.authorization.domain.models.Email
import app.cashadvisor.authorization.domain.models.Password

data class SignupDataState(
    val email: Email = Email(""),
    val password: Password = Password("password"),
    val emailCode: ConfirmCode = ConfirmCode(""),
    val loginCode: ConfirmCode = ConfirmCode(""),
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isPasswordLengthValid: Boolean = false,
    val isConfirmPasswordValid: Boolean = false,
    val isEmailCodeValid: Boolean = false,
    val isLoginCodeValid: Boolean = false,
    val isRegisterInProgress: Boolean = false,
    val isLoginInProgress: Boolean = false
)