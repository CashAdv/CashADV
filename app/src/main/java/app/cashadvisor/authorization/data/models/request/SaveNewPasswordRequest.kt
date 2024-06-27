package app.cashadvisor.authorization.data.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveNewPasswordRequest(
    val email:String,
    val password:String,
    @SerialName("reset_token") val resetToken:String
)