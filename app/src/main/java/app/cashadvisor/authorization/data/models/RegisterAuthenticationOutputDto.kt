package app.cashadvisor.authorization.data.models

data class RegisterAuthenticationOutputDto(
    val message: String,
    val statusCode: Int,
    val accessTokenLifeTime: Long,
    val refreshTokenLifeTime: Long,
    val deviceId: String,
    val tokenDetailsDto: TokenDetailsOutputDto
)