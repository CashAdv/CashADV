package app.cashadvisor.authorization.domain.models

data class RegisterAuthenticationData(
    val message: String,
    val accessTokenLifeTime: Long,
    val refreshTokenLifeTime: Long,
    val tokenDetails: TokenDetails
)