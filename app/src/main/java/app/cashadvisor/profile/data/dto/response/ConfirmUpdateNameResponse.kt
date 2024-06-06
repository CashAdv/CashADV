package app.cashadvisor.profile.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfirmUpdateNameResponse(
    val message: String,
    @SerialName("status_code") val statusCode: Int
)