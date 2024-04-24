package app.cashadvisor.profile.data.impl

import app.cashadvisor.profile.data.api.ProfileInfoStorage
import app.cashadvisor.profile.data.dto.UserInfoDto
import kotlinx.coroutines.sync.Mutex

class ProfileInfoStorageImpl : ProfileInfoStorage {

    private val profileInfoMutex = Mutex()
    private var profileInfo: UserInfoDto? = null

    override suspend fun getProfileInfo(): UserInfoDto? {
        profileInfoMutex.lock()
        val copy = profileInfo?.copy()
        profileInfoMutex.unlock()
        return copy
    }

    override suspend fun saveProfileInfo(userInfoDto: UserInfoDto) {
        profileInfoMutex.lock()
        profileInfo = userInfoDto.copy()
        profileInfoMutex.unlock()
    }

    override suspend fun updateUserName(name: String, surname: String) {
        profileInfoMutex.lock()
        profileInfo = profileInfo?.copy(name = name, surname = surname)
        profileInfoMutex.unlock()
    }

    override suspend fun updateProfilePic(picUrl: String) {
        profileInfoMutex.lock()
        profileInfo = profileInfo?.copy(profilePicUrl = picUrl)
        profileInfoMutex.unlock()
    }
}