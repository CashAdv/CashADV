package app.cashadvisor.profile.domain.model

data class UserProfileInfo(
    val id: String,
    val name: String = "",
    val surname: String = "",
    val profilePicUrl: String? = null
)
