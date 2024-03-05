package app.cashadvisor.authorization.data.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfirmLoginResponse(
    val message: String,
    @SerialName("status_code") val statusCode: Int,
    @SerialName("access_token_life_time") val accessTokenLifeTime: Long,
    @SerialName("refresh_token_life_time") val refreshTokenLifeTime: Long,
    @SerialName("device_id") val deviceId: String,
    @SerialName("token_details") val tokenDetailsDto: TokenDetailsDto
)