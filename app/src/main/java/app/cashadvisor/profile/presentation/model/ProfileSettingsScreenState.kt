package app.cashadvisor.profile.presentation.model

import android.net.Uri
import app.cashadvisor.profile.domain.api.InputValidationState
import app.cashadvisor.profile.domain.model.UserProfileInfo

sealed interface ProfileSettingsScreenState {

    data object Default : ProfileSettingsScreenState

    data class UserData(
        val name: String,
        val surname: String,
        val profilePicUrl: String?,
    ): ProfileSettingsScreenState

    data class InputValidation(
        val profilePicUrl: String?,
        val nameInputValidationState: InputValidationState,
        val surnameInputValidationState: InputValidationState
    ): ProfileSettingsScreenState

}