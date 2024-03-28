package app.cashadvisor.profile.data.impl

import android.net.Uri
import app.cashadvisor.authorization.domain.api.CredentialsRepository
import app.cashadvisor.common.domain.Resource
import app.cashadvisor.common.domain.model.ErrorEntity
import app.cashadvisor.profile.data.api.ProfileInfoRemoteDataSource
import app.cashadvisor.profile.data.dto.request.UpdateProfilePicRequest
import app.cashadvisor.profile.data.dto.request.UpdateUserNameRequest
import app.cashadvisor.profile.domain.api.ProfileInfoRepository
import app.cashadvisor.profile.domain.model.UserProfileInfo
import java.io.File
import javax.inject.Inject

class ProfileInfoRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProfileInfoRemoteDataSource,
    private val credentialsRepository: CredentialsRepository
) : ProfileInfoRepository {

    private suspend fun getAccessToken(): String {
        return credentialsRepository.getCredentials()?.accessToken ?: ""
    }
    override suspend fun getUserInfo(): Resource<UserProfileInfo> {
        return try {
            val response = remoteDataSource.getUserInfo(getAccessToken())
            Resource.Success(UserProfileInfo(response.userInfo!!.name, response.userInfo.surname, response.userInfo.profilePicUrl))
        } catch (exception: Exception) {
            // TODO: создать маппер
            Resource.Error(ErrorEntity.UnknownError(exception.message.toString()))
        }
    }

    override suspend fun updateUserName(
        name: String,
        surname: String
    ): Resource<Unit> {
        return try {
            val response = remoteDataSource.updateUserName(
                accessToken = getAccessToken(),
                dto = UpdateUserNameRequest(name, surname)
            )
            Resource.Success(Unit)
        } catch (exception: Exception) {
            // TODO: создать маппер
            Resource.Error(ErrorEntity.UnknownError(exception.message.toString()))
        }
    }

    override suspend fun updateProfilePic(profilePic: Uri): Resource<Unit> {
        return try {
            val picFile = profilePic.path?.let { File(it) }
            val response = remoteDataSource.updateProfilePic(
                accessToken = getAccessToken(),
                dto = UpdateProfilePicRequest(profilePic = picFile!!) // TODO: временно, добавить обработку ошибки
            )
            Resource.Success(Unit)
        } catch (exception: Exception) {
            // TODO: создать маппер
            Resource.Error(ErrorEntity.UnknownError(exception.message.toString()))
        }
    }
}