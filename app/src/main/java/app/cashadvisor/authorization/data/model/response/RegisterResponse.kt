package app.cashadvisor.authorization.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val message: String,
    @SerialName("token") val token: String,
    @SerialName("status_code") val statusCode: Int = 0,
)