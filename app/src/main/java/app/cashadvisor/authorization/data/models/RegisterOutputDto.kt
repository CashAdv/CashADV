package app.cashadvisor.authorization.data.models

data class RegisterOutputDto (
    val message: String,
    val token: String,
    val statusCode: Int
)