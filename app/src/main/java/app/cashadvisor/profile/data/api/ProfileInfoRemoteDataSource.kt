package app.cashadvisor.profile.data.api

import app.cashadvisor.profile.data.dto.request.UpdateProfilePicRequest
import app.cashadvisor.profile.data.dto.request.UpdateUserNameRequest
import app.cashadvisor.profile.data.dto.response.ConfirmUpdateNameResponse
import app.cashadvisor.profile.data.dto.response.ConfirmUpdatePicResponse
import app.cashadvisor.profile.data.dto.response.ProfileInfoResponse

interface ProfileInfoRemoteDataSource {

    suspend fun getUserInfo(accessToken: String): ProfileInfoResponse

    suspend fun updateUserName(
        dto: UpdateUserNameRequest,
        accessToken: String
    ): ConfirmUpdateNameResponse

    suspend fun updateProfilePic(
        dto: UpdateProfilePicRequest,
        accessToken: String
    ): ConfirmUpdatePicResponse

}