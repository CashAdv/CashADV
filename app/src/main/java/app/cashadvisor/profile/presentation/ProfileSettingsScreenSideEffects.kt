package app.cashadvisor.profile.presentation

sealed interface ProfileSettingsScreenSideEffects {
    data object IncorrectName: ProfileSettingsScreenSideEffects

    data object IncorrectSurname: ProfileSettingsScreenSideEffects

    data object IncorrectNameAndSurname: ProfileSettingsScreenSideEffects

    data object NoInternetConnection: ProfileSettingsScreenState

    data object FailedToSaveData: ProfileSettingsScreenState
}