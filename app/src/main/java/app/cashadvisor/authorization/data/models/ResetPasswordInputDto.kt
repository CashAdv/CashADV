package app.cashadvisor.authorization.data.models


data class ResetPasswordInputDto(
    val code: String,
    val token: String
)
