package app.cashadvisor.profile.domain.model

import android.net.Uri

data class UserProfileInfo(
    val name: String = "",
    val surname: String = "",
    val profilePicUrl: Uri? = null
)
