package app.cashadvisor.profile.presentation.model

import app.cashadvisor.profile.domain.api.InputValidationState

sealed interface ProfileSettingsScreenState {

    data object Default : ProfileSettingsScreenState

    data class UserData(
        val name: String,
        val surname: String,
        val profilePicUrl: String?,
    ) : ProfileSettingsScreenState

    data class InputValidation(
        val profilePicUrl: String?,
        val nameInputValidationState: InputValidationState,
        val surnameInputValidationState: InputValidationState
    ) : ProfileSettingsScreenState

}