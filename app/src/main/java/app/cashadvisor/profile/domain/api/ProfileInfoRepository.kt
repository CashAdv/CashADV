package app.cashadvisor.profile.domain.api

import android.net.Uri
import app.cashadvisor.common.domain.Resource
import app.cashadvisor.profile.domain.model.UserProfileInfo

interface ProfileInfoRepository {
    suspend fun getUserInfo(): Resource<UserProfileInfo>

    suspend fun updateUserName(
        name: String,
        surname: String
    ): Resource<Unit>

    suspend fun updateProfilePic(
        profilePic: Uri
    ): Resource<Unit>
}