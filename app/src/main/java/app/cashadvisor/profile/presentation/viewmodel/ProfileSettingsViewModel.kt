package app.cashadvisor.profile.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import app.cashadvisor.common.ui.BaseViewModel
import app.cashadvisor.profile.domain.api.InputValidationError
import app.cashadvisor.profile.domain.api.InputValidationInteractor
import app.cashadvisor.profile.domain.api.InputValidationState
import app.cashadvisor.profile.presentation.model.ProfileSettingsScreenSideEffects
import app.cashadvisor.profile.presentation.model.ProfileSettingsScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    init {
        // будем загружать данные пользователя при создании вью модели, пока пустой
        viewModelScope.launch {
            _uiState.emit(
                ProfileSettingsScreenState.UserData(
                    name = "",
                    surname = "",
                    profilePicUrl = null
                )
            )
        }
    }

    fun saveChanges(
        name: String,
        surname: String,
        profilePicUrl: String?
    ) {
        viewModelScope.launch {
            nameValidationState = inputValidationInteractor.validateName(name)
            surnameValidationState = inputValidationInteractor.validateSurname(surname)
            picUrl = profilePicUrl

            if (isInputValid()) {
                // Сохранить
                delay(1000)
                _uiState.value = ProfileSettingsScreenState.UserData(
                    name = name,
                    surname = surname,
                    profilePicUrl = profilePicUrl
                )
                nameValidationState = InputValidationState.Default
                surnameValidationState = InputValidationState.Default
                _sideEffects.emit(ProfileSettingsScreenSideEffects.DataSaved)
            } else {
                _uiState.value = ProfileSettingsScreenState.InputValidation(
                    profilePicUrl = profilePicUrl,
                    nameInputValidationState = nameValidationState,
                    surnameInputValidationState = surnameValidationState,
                )
                emitErrorMessage()
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
}