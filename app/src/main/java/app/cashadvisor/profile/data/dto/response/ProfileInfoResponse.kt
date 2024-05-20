package app.cashadvisor.profile.data.dto.response

import app.cashadvisor.profile.data.dto.UserInfoDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileInfoResponse(
    @SerialName("status_code") val statusCode: Int,
    val message: String,
    @SerialName("profile") val userInfo: UserInfoDto?
)