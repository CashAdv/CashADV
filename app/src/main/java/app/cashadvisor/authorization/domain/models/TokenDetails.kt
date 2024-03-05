package app.cashadvisor.authorization.domain.models

data class TokenDetails(
    val accessToken: String,
    val refreshToken: String,
    val expiredAt: Long
)