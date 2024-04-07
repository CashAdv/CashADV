package app.cashadvisor.profile.data.api

import app.cashadvisor.profile.data.dto.UserInfoDto

interface ProfileInfoStorage {

    suspend fun getProfileInfo(): UserInfoDto?

    suspend fun updateUserName(name: String, surname: String)

    suspend fun updateProfilePic(picUrl: String)
}