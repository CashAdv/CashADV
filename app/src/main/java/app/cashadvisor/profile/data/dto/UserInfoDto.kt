package app.cashadvisor.profile.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoDto(
    val surname: String,
    val name: String,
    @SerialName("user_id") val id: String,
    @SerialName("avatar_url") val profilePicUrl: String?
)
