package app.cashadvisor.profile.data.mapper

import app.cashadvisor.profile.data.dto.UserInfoDto
import app.cashadvisor.profile.domain.model.UserProfileInfo
import javax.inject.Inject

class ProfileInfoMapper @Inject constructor() {
    fun mapToDomain(userInfoDto: UserInfoDto) = UserProfileInfo(
        id = userInfoDto.id,
        name = userInfoDto.name,
        surname = userInfoDto.surname,
        profilePicUrl = userInfoDto.profilePicUrl
    )
}