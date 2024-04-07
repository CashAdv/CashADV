package app.cashadvisor.profile.data.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import app.cashadvisor.profile.data.api.ProfileInfoStorage
import app.cashadvisor.profile.data.dto.UserInfoDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ProfileInfoStorageImpl(
    private val storage: SharedPreferences,
    private val key: String,
    private val json: Json
) : ProfileInfoStorage {
    override suspend fun getProfileInfo(): UserInfoDto? {
        return storage.getString(key, null)?.let {
            json.decodeFromString<UserInfoDto>(it)
        }
    }

    override suspend fun updateUserName(name: String, surname: String) {
        val currentInfo = getProfileInfo()
        currentInfo?.let {
            val data = json.encodeToString(
                it.copy(name = name, surname = surname)
            )
            storage.edit {
                putString(key, data)
            }
        }
    }

    override suspend fun updateProfilePic(picUrl: String) {
        val currentInfo = getProfileInfo()
        currentInfo?.let {
            val data = json.encodeToString(
                it.copy(profilePicUrl = picUrl)
            )
            storage.edit {
                putString(key, data)
            }
        }
    }
}