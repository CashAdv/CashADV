package app.cashadvisor.authorization.data.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveNewPasswordResponse(
    val message:String,
    @SerialName("status_code") val statusCode: Int = 0

)
