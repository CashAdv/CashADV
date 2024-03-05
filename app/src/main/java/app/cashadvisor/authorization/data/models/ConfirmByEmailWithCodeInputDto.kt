package app.cashadvisor.authorization.data.models

data class ConfirmByEmailWithCodeInputDto(
    val email: String,
    val token: String,
    val code: String
)