package app.cashadvisor.profile.data.impl

import android.content.Context
import android.net.Uri
import app.cashadvisor.authorization.domain.api.CredentialsRepository
import app.cashadvisor.common.domain.BaseExceptionToErrorMapper
import app.cashadvisor.common.domain.Resource
import app.cashadvisor.common.domain.model.ErrorEntity
import app.cashadvisor.profile.data.api.ProfileInfoRemoteDataSource
import app.cashadvisor.profile.data.api.ProfileInfoStorage
import app.cashadvisor.profile.data.dto.UserInfoDto
import app.cashadvisor.profile.data.dto.request.UpdateProfilePicRequest
import app.cashadvisor.profile.data.dto.request.UpdateUserNameRequest
import app.cashadvisor.profile.data.mapper.ProfileInfoMapper
import app.cashadvisor.profile.domain.api.ProfileInfoRepository
import app.cashadvisor.profile.domain.model.UserProfileInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class ProfileInfoRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
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
            if (isEmptyProfile(response.userInfo!!)) {
                return Resource.Error(ErrorEntity.Profile.EmptyProfile())
            }
            storage.saveProfileInfo(userInfoDto = response.userInfo)
            Resource.Success(mapper.mapToDomain(response.userInfo))
        } catch (exception: Exception) {
            Resource.Error(
                profileExceptionToErrorMapper.handleException(exception)
            )
        }
    }

    private fun isEmptyProfile(userProfile: UserInfoDto): Boolean {
        return with(userProfile) {
            surname.isBlank() && name == BLANK_PROFILE_NAME && profilePicUrl == null
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

        val inputStream = context.contentResolver.openInputStream(profilePic)
        val file = createTemporaryFile(inputStream)



        return try {
            val response = remoteDataSource.updateProfilePic(
                dto = UpdateProfilePicRequest(profilePic = file),
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


    private fun createTemporaryFile(inputStream: InputStream?): File {
        val file = File.createTempFile("temp_image", null, context.cacheDir)
        file.deleteOnExit()

        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }

        return file
    }

    companion object {
        private const val BLANK_PROFILE_NAME = "Мы тебя не знаем..."
    }
}
