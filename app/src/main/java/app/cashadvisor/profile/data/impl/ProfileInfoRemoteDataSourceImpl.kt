package app.cashadvisor.profile.data.impl

import app.cashadvisor.common.utill.exceptions.NetworkException
import app.cashadvisor.profile.data.api.ProfileInfoApiService
import app.cashadvisor.profile.data.api.ProfileInfoRemoteDataSource
import app.cashadvisor.profile.data.dto.request.UpdateProfilePicRequest
import app.cashadvisor.profile.data.dto.request.UpdateUserNameRequest
import app.cashadvisor.profile.data.dto.response.ConfirmUpdateNameResponse
import app.cashadvisor.profile.data.dto.response.ConfirmUpdatePicResponse
import app.cashadvisor.profile.data.dto.response.ProfileInfoResponse
import javax.inject.Inject

class ProfileInfoRemoteDataSourceImpl @Inject constructor(
    private val profileInfoApiService: ProfileInfoApiService
) : ProfileInfoRemoteDataSource {
    override suspend fun getUserInfo(accessToken: String): ProfileInfoResponse {
        return try {
            profileInfoApiService.getUserInfo(accessToken = "Bearer $accessToken")
        } catch (exception: NetworkException) {
            // TODO: обработать исключение
            throw exception
        }
    }

    override suspend fun updateUserName(
        dto: UpdateUserNameRequest,
        accessToken: String
    ): ConfirmUpdateNameResponse {
        return try {
            profileInfoApiService.updateUserName(
                updateUserNameRequest = dto,
                accessToken = "Bearer $accessToken"
            )
        } catch (exception: NetworkException) {
            // TODO: обработать исключение
            throw exception
        }
    }

    override suspend fun updateProfilePic(
        dto: UpdateProfilePicRequest,
        accessToken: String
    ): ConfirmUpdatePicResponse {
        return try {
            profileInfoApiService.updateProfilePic(
                updateProfilePicRequest = dto,
                accessToken = "Bearer $accessToken"
            )
        } catch (exception: NetworkException) {
            // TODO: обработать исключение
            throw exception
        }
    }
}