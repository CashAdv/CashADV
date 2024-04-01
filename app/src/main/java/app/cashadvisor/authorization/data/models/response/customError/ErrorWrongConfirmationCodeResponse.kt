package app.cashadvisor.authorization.data.models.response.customError

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorWrongConfirmationCodeResponse(
    @SerialName("remaining_attempts") val remainingAttempts: Int,
    @SerialName("lock_duration") val lockDuration: Long,
    val error: String,
    @SerialName("status_code") val statusCode: Int
)