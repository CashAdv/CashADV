package app.cashadvisor.authorization.data.models

data class LoginOutputDto(
    val message: String,
    val token: String,
    val statusCode: Int
)