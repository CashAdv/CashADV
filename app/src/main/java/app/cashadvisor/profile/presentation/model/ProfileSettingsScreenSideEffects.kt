package app.cashadvisor.profile.presentation.model

sealed interface ProfileSettingsScreenSideEffects {
    data object IncorrectName: ProfileSettingsScreenSideEffects

    data object IncorrectSurname: ProfileSettingsScreenSideEffects

    data object IncorrectNameAndSurname: ProfileSettingsScreenSideEffects

    data object NoInternetConnection: ProfileSettingsScreenState

    data object FailedToSaveData: ProfileSettingsScreenState
}