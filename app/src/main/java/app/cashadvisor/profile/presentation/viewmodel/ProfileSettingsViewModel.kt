package app.cashadvisor.profile.presentation.viewmodel

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import app.cashadvisor.common.domain.Resource
import app.cashadvisor.common.domain.model.ErrorEntity
import app.cashadvisor.common.ui.BaseViewModel
import app.cashadvisor.common.utils.debounce
import app.cashadvisor.profile.domain.api.InputValidationError
import app.cashadvisor.profile.domain.api.InputValidationInteractor
import app.cashadvisor.profile.domain.api.InputValidationState
import app.cashadvisor.profile.domain.api.ProfileInfoInteractor
import app.cashadvisor.profile.presentation.model.ProfileSettingsScreenSideEffects
import app.cashadvisor.profile.presentation.model.ProfileSettingsScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSettingsViewModel @Inject constructor(
    private val inputValidationInteractor: InputValidationInteractor,
    private val profileInfoInteractor: ProfileInfoInteractor
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<ProfileSettingsScreenState> =
        MutableStateFlow(ProfileSettingsScreenState.Default)
    val uiState: StateFlow<ProfileSettingsScreenState> = _uiState.asStateFlow()

    private val _sideEffects: MutableSharedFlow<ProfileSettingsScreenSideEffects> =
        MutableSharedFlow()
    val sideEffects: SharedFlow<ProfileSettingsScreenSideEffects> = _sideEffects.asSharedFlow()

    private var nameValidationState: InputValidationState = InputValidationState.Default
    private var surnameValidationState: InputValidationState = InputValidationState.Default
    private var picUrl: String? = null
    private var nameInput = ""
    private var surnameInput = ""

    private val saveProfileDebounce =
        debounce<Triple<String, String, String?>>(
            DEBOUNCE_IN_MILLIS,
            viewModelScope,
            true
        ) { newInfo ->
            saveProfileRequest(newInfo)
        }

    init {
        getProfileInfo()
    }

    private fun getProfileInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = profileInfoInteractor.getUserInfo()) {
                is Resource.Error -> {
                    handleGetProfileError(result.error)
                }

                is Resource.Success -> {
                    _uiState.value = ProfileSettingsScreenState.UserData(
                        name = result.data.name,
                        surname = result.data.surname,
                        profilePicUrl = result.data.profilePicUrl
                    )
                    picUrl = result.data.profilePicUrl
                }
            }
        }
    }

    private suspend fun handleGetProfileError(error: ErrorEntity) {
        when (error) {
            is ErrorEntity.Profile.FailedToGetData -> {
                _sideEffects.emit(ProfileSettingsScreenSideEffects.FailedToGetData)

            }

            is ErrorEntity.NetworksError.NoInternet -> {
                _sideEffects.emit(ProfileSettingsScreenSideEffects.NoInternetConnection)
            }

            is ErrorEntity.Profile.EmptyProfile -> {
                _uiState.emit(ProfileSettingsScreenState.Default)
            }

            else -> {
                _sideEffects.emit(ProfileSettingsScreenSideEffects.UndefinedError)
            }
        }
    }

    fun saveChanges(
        name: String,
        surname: String,
        profilePicUrl: String?
    ) {
        saveProfileDebounce(Triple(name, surname, profilePicUrl))
    }

    private fun saveProfileRequest(data: Triple<String, String, String?>) {
        val (name, surname, profilePicUrl) = data

        viewModelScope.launch {
            nameValidationState = inputValidationInteractor.validateName(name)
            surnameValidationState = inputValidationInteractor.validateSurname(surname)

            if (isInputValid()) {
                nameValidationState = InputValidationState.Default
                surnameValidationState = InputValidationState.Default

                var savePicResult: Resource<Unit>? = null
                if (profilePicUrl != picUrl) {
                    profilePicUrl?.let {
                        savePicResult = profileInfoInteractor.updateProfilePic(it.toUri())
                    }
                }
                val saveNameResult = profileInfoInteractor.updateUserName(name, surname)

                processSaveResult(savePicResult, saveNameResult)
            } else {
                picUrl = profilePicUrl

                _uiState.value = ProfileSettingsScreenState.InputValidation(
                    profilePicUrl = profilePicUrl,
                    nameInputValidationState = nameValidationState,
                    surnameInputValidationState = surnameValidationState,
                )
                emitErrorMessage()
            }
        }
    }

    private suspend fun processSaveResult(
        savePicResult: Resource<Unit>?,
        saveNameResult: Resource<Unit>
    ) {
        when {
            (savePicResult is Resource.Success || savePicResult == null)
                    && saveNameResult is Resource.Success -> {
                _sideEffects.emit(ProfileSettingsScreenSideEffects.DataSaved)
                getProfileInfo()
            }

            (savePicResult is Resource.Success || savePicResult == null)
                    && saveNameResult is Resource.Error -> {
                _sideEffects.emit(
                    if (saveNameResult.error is ErrorEntity.NetworksError.NoInternet) {
                        ProfileSettingsScreenSideEffects.NoInternetConnection
                    } else {
                        ProfileSettingsScreenSideEffects.FailedToUpdateUsername
                    }
                )
            }

            savePicResult is Resource.Error && saveNameResult is Resource.Error -> {
                _sideEffects.emit(
                    if (saveNameResult.error is ErrorEntity.NetworksError.NoInternet) {
                        ProfileSettingsScreenSideEffects.NoInternetConnection
                    } else {
                        ProfileSettingsScreenSideEffects.FailedToSaveData
                    }
                )
            }

            savePicResult is Resource.Error -> {
                _sideEffects.emit(
                    if (savePicResult.error is ErrorEntity.NetworksError.NoInternet) {
                        ProfileSettingsScreenSideEffects.NoInternetConnection
                    } else {
                        ProfileSettingsScreenSideEffects.FailedToUpdateProfilePic
                    }
                )
            }
        }
    }

    private fun isInputValid(): Boolean {
        return (nameValidationState is InputValidationState.Success &&
                surnameValidationState !is InputValidationState.Error)
    }

    private suspend fun emitErrorMessage() {
        val isInvalidName = nameValidationState is InputValidationState.Error
        val isInvalidSurname = surnameValidationState is InputValidationState.Error
        when {
            isInvalidName && isInvalidSurname -> {
                _sideEffects.emit(ProfileSettingsScreenSideEffects.IncorrectNameAndSurname)
            }

            isInvalidName -> {
                _sideEffects.emit(
                    if ((nameValidationState as InputValidationState.Error).error == InputValidationError.EMPTY_VALUE) {
                        ProfileSettingsScreenSideEffects.EmptyName
                    } else {
                        ProfileSettingsScreenSideEffects.IncorrectName
                    }
                )
            }

            isInvalidSurname -> {
                _sideEffects.emit(ProfileSettingsScreenSideEffects.IncorrectSurname)
            }
        }
    }

    fun updateInput(name: String? = null, surname: String? = null) {
        if (name == nameInput || surname == surnameInput) return

        name?.let {
            nameValidationState = InputValidationState.Default
            nameInput = it
        }
        surname?.let {
            surnameValidationState = InputValidationState.Default
            surnameInput = it
        }

        _uiState.value = ProfileSettingsScreenState.InputValidation(
            profilePicUrl = picUrl,
            nameInputValidationState = nameValidationState,
            surnameInputValidationState = surnameValidationState
        )
    }

    companion object {
        private const val DEBOUNCE_IN_MILLIS = 1000L
    }
}