package app.cashadvisor.profile.domain.api

import app.cashadvisor.common.domain.Resource
import app.cashadvisor.profile.data.dto.UserInfoDto
import java.io.File

interface ProfileInfoRepository {
    suspend fun getUserInfo(accessToken: String): Resource<UserInfoDto>

    suspend fun updateUserName(
        name: String,
        surname: String,
        accessToken: String
    ): Resource<Unit>

    suspend fun updateProfilePic(
        profilePic: File,
        accessToken: String
    ): Resource<Unit>
}