package app.cashadvisor.authorization.domain.models.states

data class PasswordRecoveryUiState(
    val emailIsValid:Boolean = false,
    val confirmEmailCodeIsValid:Boolean = false,
    val newPasswordIsValid:Boolean = false,
    val message:String = ""
)
