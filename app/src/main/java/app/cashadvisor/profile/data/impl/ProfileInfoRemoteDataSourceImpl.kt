package app.cashadvisor.profile.data.impl

import app.cashadvisor.common.utill.exceptions.NetworkException
import app.cashadvisor.profile.data.mapper.NetworkToProfileExceptionMapper
import app.cashadvisor.profile.data.api.ProfileInfoApiService
import app.cashadvisor.profile.data.api.ProfileInfoRemoteDataSource
import app.cashadvisor.profile.data.dto.request.UpdateProfilePicRequest
import app.cashadvisor.profile.data.dto.request.UpdateUserNameRequest
import app.cashadvisor.profile.data.dto.response.ConfirmUpdateNameResponse
import app.cashadvisor.profile.data.dto.response.ConfirmUpdatePicResponse
import app.cashadvisor.profile.data.dto.response.ProfileInfoResponse
import javax.inject.Inject


class ProfileInfoRemoteDataSourceImpl @Inject constructor(
    private val profileInfoApiService: ProfileInfoApiService,
    private val networkToProfileExceptionMapper: NetworkToProfileExceptionMapper
) : ProfileInfoRemoteDataSource {
    override suspend fun getUserInfo(accessToken: String): ProfileInfoResponse {
        return try {
            val response = profileInfoApiService.getUserInfo(accessToken = accessToken)
            response
        } catch (exception: NetworkException) {
            throw networkToProfileExceptionMapper.handleExceptionGettingProfile(exception)
        }
    }

    override suspend fun updateUserName(
        dto: UpdateUserNameRequest,
        accessToken: String
    ): ConfirmUpdateNameResponse {
        return try {
            profileInfoApiService.updateUserName(
                updateUserNameRequest = dto,
                accessToken = accessToken
            )
        } catch (exception: NetworkException) {
            throw networkToProfileExceptionMapper.handleExceptionUpdatingProfile(exception)
        }
    }

    override suspend fun updateProfilePic(
        dto: UpdateProfilePicRequest,
        accessToken: String
    ): ConfirmUpdatePicResponse {
        return try {
            profileInfoApiService.updateProfilePic(accessToken, dto)
        } catch (exception: NetworkException) {
            throw networkToProfileExceptionMapper.handleExceptionUpdatingProfile(exception)
        }
    }
}