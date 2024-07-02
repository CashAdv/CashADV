package app.cashadvisor.profile.domain.impl

import android.net.Uri
import app.cashadvisor.common.domain.Resource
import app.cashadvisor.profile.domain.api.ProfileInfoInteractor
import app.cashadvisor.profile.domain.api.ProfileInfoRepository
import app.cashadvisor.profile.domain.model.ProfileAnalytics
import app.cashadvisor.profile.domain.model.UserInfoMore
import app.cashadvisor.profile.domain.model.UserProfileInfo
import javax.inject.Inject

class ProfileInfoInteractorImpl @Inject constructor(
    private val profileInfoRepository: ProfileInfoRepository
) : ProfileInfoInteractor {
    override suspend fun getUserInfo(): Resource<UserProfileInfo> {
        return profileInfoRepository.getUserInfo()
    }

    override suspend fun updateUserName(name: String, surname: String): Resource<Unit> {
        return profileInfoRepository.updateUserName(name, surname)
    }

    override suspend fun updateProfilePic(profilePic: Uri): Resource<Unit> {
        return profileInfoRepository.updateProfilePic(profilePic)
    }

    override suspend fun getUserInfoMore(): Resource<UserInfoMore> {
        return profileInfoRepository.getUserInfoMore()
    }

    override suspend fun getUserAnalytics(): Resource<ProfileAnalytics> {
        return profileInfoRepository.getUserAnalytics()
    }

}