package app.cashadvisor.profile.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserNameRequest(
    val name: String,
    val surname: String
)
