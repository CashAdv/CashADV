package app.cashadvisor.profile.presentation.model

sealed interface ProfileSettingsScreenSideEffects {
    data object IncorrectName : ProfileSettingsScreenSideEffects

    data object EmptyName : ProfileSettingsScreenSideEffects

    data object IncorrectSurname : ProfileSettingsScreenSideEffects

    data object IncorrectNameAndSurname : ProfileSettingsScreenSideEffects

    data object NoInternetConnection : ProfileSettingsScreenSideEffects

    data object FailedToSaveData : ProfileSettingsScreenSideEffects

    data object FailedToUpdateUsername : ProfileSettingsScreenSideEffects

    data object FailedToUpdateProfilePic : ProfileSettingsScreenSideEffects

    data object DataSaved : ProfileSettingsScreenSideEffects
}