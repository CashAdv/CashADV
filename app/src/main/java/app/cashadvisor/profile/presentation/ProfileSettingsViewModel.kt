package app.cashadvisor.profile.presentation

import android.net.Uri
import app.cashadvisor.common.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileSettingsViewModel : BaseViewModel() {

    private val _uiState: MutableStateFlow<ProfileSettingsScreenState> =
        MutableStateFlow(ProfileSettingsScreenState.Default)

    val uiState: StateFlow<ProfileSettingsScreenState> = _uiState.asStateFlow()

    private val _sideEffects: MutableSharedFlow<ProfileSettingsScreenSideEffects> = MutableSharedFlow()

    val sideEffects: SharedFlow<ProfileSettingsScreenSideEffects> = _sideEffects.asSharedFlow()

    fun saveChanges(
        name: String?,
        surname: String?,
        profilePicUri: Uri?
    ) {

    }
}