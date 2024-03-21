package app.cashadvisor.profile.presentation.model

import app.cashadvisor.profile.domain.model.UserProfileInfo

sealed interface ProfileSettingsScreenState {

    data object Default : ProfileSettingsScreenState

    data class Content(val profileInfo: UserProfileInfo) : ProfileSettingsScreenState
}