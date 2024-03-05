package app.cashadvisor.authorization.data.models

data class TokenDetailsOutputDto(
    val accessToken: String,
    val refreshToken: String,
    val expiredAt: Long
)