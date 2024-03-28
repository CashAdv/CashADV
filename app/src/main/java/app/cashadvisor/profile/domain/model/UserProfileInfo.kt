package app.cashadvisor.profile.domain.model

data class UserProfileInfo(
    val name: String = "",
    val surname: String = "",
    val profilePicUrl: String? = null
)
