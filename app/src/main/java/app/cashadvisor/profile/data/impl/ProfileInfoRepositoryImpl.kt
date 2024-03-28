package app.cashadvisor.profile.data.impl

import app.cashadvisor.common.domain.Resource
import app.cashadvisor.common.domain.model.ErrorEntity
import app.cashadvisor.profile.data.api.ProfileInfoRemoteDataSource
import app.cashadvisor.profile.data.dto.UserInfoDto
import app.cashadvisor.profile.data.dto.request.UpdateProfilePicRequest
import app.cashadvisor.profile.data.dto.request.UpdateUserNameRequest
import app.cashadvisor.profile.domain.api.ProfileInfoRepository
import java.io.File
import javax.inject.Inject

class ProfileInfoRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProfileInfoRemoteDataSource
) : ProfileInfoRepository {
    override suspend fun getUserInfo(accessToken: String): Resource<UserInfoDto> {
        return try {
            val response = remoteDataSource.getUserInfo(accessToken)
            Resource.Success(response.userInfo!!)
        } catch (exception: Exception) {
            // TODO: создать маппер
            Resource.Error(ErrorEntity.UnknownError(exception.message.toString()))
        }
    }

    override suspend fun updateUserName(
        name: String,
        surname: String,
        accessToken: String
    ): Resource<Unit> {
        return try {
            val response = remoteDataSource.updateUserName(
                accessToken = accessToken,
                dto = UpdateUserNameRequest(name, surname)
            )
            Resource.Success(Unit)
        } catch (exception: Exception) {
            // TODO: создать маппер
            Resource.Error(ErrorEntity.UnknownError(exception.message.toString()))
        }
    }

    override suspend fun updateProfilePic(profilePic: File, accessToken: String): Resource<Unit> {
        return try {
            val response = remoteDataSource.updateProfilePic(
                accessToken = accessToken,
                dto = UpdateProfilePicRequest(profilePic = profilePic)
            )
            Resource.Success(Unit)
        } catch (exception: Exception) {
            // TODO: создать маппер
            Resource.Error(ErrorEntity.UnknownError(exception.message.toString()))
        }
    }
}