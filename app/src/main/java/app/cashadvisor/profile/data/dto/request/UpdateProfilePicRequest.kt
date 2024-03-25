package app.cashadvisor.profile.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfilePicRequest(
    //TODO: разобраться с типом, должен быть File?
    @SerialName("file") val profilePic: String
)
