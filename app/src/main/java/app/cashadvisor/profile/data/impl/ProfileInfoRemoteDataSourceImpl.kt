package app.cashadvisor.profile.data.impl

import app.cashadvisor.common.utill.exceptions.NetworkException
import app.cashadvisor.profile.data.NetworkToProfileExceptionMapper
import app.cashadvisor.profile.data.api.ProfileInfoApiService
import app.cashadvisor.profile.data.api.ProfileInfoRemoteDataSource
import app.cashadvisor.profile.data.dto.request.UpdateProfilePicRequest
import app.cashadvisor.profile.data.dto.request.UpdateUserNameRequest
import app.cashadvisor.profile.data.dto.response.ConfirmUpdateNameResponse
import app.cashadvisor.profile.data.dto.response.ConfirmUpdatePicResponse
import app.cashadvisor.profile.data.dto.response.ProfileInfoResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
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
            val file = dto.profilePic
            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("profile_picture", file.name, requestFile)
            profileInfoApiService.updateProfilePic(
                updateProfilePicRequest = body,
                accessToken = accessToken
            )
        } catch (exception: NetworkException) {
            throw networkToProfileExceptionMapper.handleExceptionUpdatingProfile(exception)
        }
    }
}