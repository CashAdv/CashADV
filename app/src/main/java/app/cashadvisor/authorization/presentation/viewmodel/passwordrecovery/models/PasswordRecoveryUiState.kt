package app.cashadvisor.authorization.presentation.viewmodel.passwordrecovery.models

data class PasswordRecoveryUiState(
    val emailIsValid:Boolean = false,
    val confirmEmailCodeIsValid:Boolean = false,
    val newPasswordIsValid:Boolean = false,
    val message:String = ""
)
