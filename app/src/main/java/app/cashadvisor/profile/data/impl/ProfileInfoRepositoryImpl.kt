package app.cashadvisor.profile.data.impl

import android.net.Uri
import androidx.core.net.toFile
import app.cashadvisor.authorization.domain.api.CredentialsRepository
import app.cashadvisor.common.domain.BaseExceptionToErrorMapper
import app.cashadvisor.common.domain.Resource
import app.cashadvisor.profile.data.api.ProfileInfoRemoteDataSource
import app.cashadvisor.profile.data.api.ProfileInfoStorage
import app.cashadvisor.profile.data.dto.request.UpdateProfilePicRequest
import app.cashadvisor.profile.data.dto.request.UpdateUserNameRequest
import app.cashadvisor.profile.data.mapper.ProfileInfoMapper
import app.cashadvisor.profile.domain.api.ProfileInfoRepository
import app.cashadvisor.profile.domain.model.UserProfileInfo
import javax.inject.Inject

class ProfileInfoRepositoryImpl @Inject constructor(
    private val storage: ProfileInfoStorage,
    private val remoteDataSource: ProfileInfoRemoteDataSource,
    private val credentialsRepository: CredentialsRepository,
    private val mapper: ProfileInfoMapper,
    private val profileExceptionToErrorMapper: BaseExceptionToErrorMapper
) : ProfileInfoRepository {

    private suspend fun getAccessToken(): String {
        return credentialsRepository.getCredentials()?.accessToken ?: ""
    }

    override suspend fun getUserInfo(): Resource<UserProfileInfo> {
        storage.getProfileInfo()?.let {
            return Resource.Success(mapper.mapToDomain(it))
        }

        return try {
            val response = remoteDataSource.getUserInfo(getAccessToken())
            storage.saveProfileInfo(userInfoDto = response.userInfo!!)
            Resource.Success(mapper.mapToDomain(response.userInfo))
        } catch (exception: Exception) {
            Resource.Error(
                profileExceptionToErrorMapper.handleException(exception)
            )
        }
    }

    override suspend fun updateUserName(
        name: String,
        surname: String
    ): Resource<Unit> {
        return try {
            remoteDataSource.updateUserName(
                accessToken = getAccessToken(),
                dto = UpdateUserNameRequest(name, surname)
            )
            storage.updateUserName(name, surname)
            Resource.Success(Unit)
        } catch (exception: Exception) {
            Resource.Error(
                profileExceptionToErrorMapper.handleException(exception)
            )
        }
    }

    override suspend fun updateProfilePic(profilePic: Uri): Resource<Unit> {
        return try {
            val response = remoteDataSource.updateProfilePic(
                dto = UpdateProfilePicRequest(profilePic = profilePic.toFile()),
                accessToken = getAccessToken()
            )
            response.profilePicUrl?.let {
                storage.updateProfilePic(it)
            }
            Resource.Success(Unit)
        } catch (exception: Exception) {
            Resource.Error(
                profileExceptionToErrorMapper.handleException(exception)
            )
        }
    }
}