package app.cashadvisor.authorization.data.models.response.customError

import kotlinx.serialization.SerialName

data class ErrorWrongConfirmationCodeResponse(
    @SerialName("remaining_attempts") val remainingAttempts: Int,
    @SerialName("lock_duration") val lockDuration: Int,
    val error: String,
    @SerialName("status_code") val statusCode: Int
)