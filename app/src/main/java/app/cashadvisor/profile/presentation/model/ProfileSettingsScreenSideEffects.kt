package app.cashadvisor.profile.presentation.model

sealed interface ProfileSettingsScreenSideEffects {
    data object Incorrect: ProfileSettingsScreenSideEffects

    data object EmptyName: ProfileSettingsScreenSideEffects

    data object IncorrectSurname: ProfileSettingsScreenSideEffects

    data object IncorrectNameAndSurname: ProfileSettingsScreenSideEffects

    data object NoInternetConnection: ProfileSettingsScreenState

    data object FailedToSaveData: ProfileSettingsScreenState
}