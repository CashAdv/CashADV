package app.cashadvisor.authorization.data.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfirmResetPasswordResponse(
    val message: String? = null,
    @SerialName("status_code") val statusCode: Int = 0
)
