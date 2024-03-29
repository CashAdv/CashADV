package app.cashadvisor.profile.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toFile
import app.cashadvisor.authorization.domain.api.CredentialsRepository
import app.cashadvisor.common.domain.BaseExceptionToErrorMapper
import app.cashadvisor.common.domain.Resource
import app.cashadvisor.profile.data.ProfileInfoMapper
import app.cashadvisor.profile.data.api.ProfileInfoRemoteDataSource
import app.cashadvisor.profile.data.dto.request.UpdateProfilePicRequest
import app.cashadvisor.profile.data.dto.request.UpdateUserNameRequest
import app.cashadvisor.profile.domain.api.ProfileInfoRepository
import app.cashadvisor.profile.domain.model.UserProfileInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ProfileInfoRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteDataSource: ProfileInfoRemoteDataSource,
    private val credentialsRepository: CredentialsRepository,
    private val mapper: ProfileInfoMapper,
    private val profileExceptionToErrorMapper: BaseExceptionToErrorMapper
) : ProfileInfoRepository {

    private suspend fun getAccessToken(): String {
        return credentialsRepository.getCredentials()?.accessToken ?: ""
    }

    override suspend fun getUserInfo(): Resource<UserProfileInfo> {
        return try {
            val response = remoteDataSource.getUserInfo(getAccessToken())
            Resource.Success(mapper.mapToDomain(response.userInfo!!))
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
            Resource.Success(Unit)
        } catch (exception: Exception) {
            Resource.Error(
                profileExceptionToErrorMapper.handleException(exception)
            )
        }
    }

    override suspend fun updateProfilePic(profilePic: Uri): Resource<Unit> {
        return try {
            remoteDataSource.updateProfilePic(
                dto = UpdateProfilePicRequest(profilePic = profilePic.toFile()),
                accessToken = getAccessToken()
            )
            Resource.Success(Unit)
        } catch (exception: Exception) {
            Resource.Error(
                profileExceptionToErrorMapper.handleException(exception)
            )
        }
    }
}