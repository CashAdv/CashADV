package app.cashadvisor.authorization.data.models

data class AuthByEmailInputDto(
    val email: String,
    val password: String
)