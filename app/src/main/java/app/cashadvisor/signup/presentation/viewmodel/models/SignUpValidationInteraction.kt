package app.cashadvisor.signup.presentation.viewmodel.models

sealed interface SignUpValidationInteraction {
    data class ValidationEmail(val email: String = "") : SignUpValidationInteraction
    data class ValidationPassword(val password: String = ""): SignUpValidationInteraction
    data class ValidationConfirmPassword(val confirmPassword: String = ""): SignUpValidationInteraction
}