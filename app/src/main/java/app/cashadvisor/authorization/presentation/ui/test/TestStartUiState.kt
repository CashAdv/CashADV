package app.cashadvisor.authorization.presentation.ui.test

data class TestStartUiState(
    val emailIsValid: Boolean = false,
    val registerCodeIsValid: Boolean = false,
    val loginCodeIsValid: Boolean = false,
    val message: String = ""
)