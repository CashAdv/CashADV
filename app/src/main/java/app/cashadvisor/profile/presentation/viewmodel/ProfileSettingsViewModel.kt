package app.cashadvisor.profile.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import app.cashadvisor.common.ui.BaseViewModel
import app.cashadvisor.profile.domain.api.InputValidationError
import app.cashadvisor.profile.domain.api.InputValidationInteractor
import app.cashadvisor.profile.domain.api.InputValidationState
import app.cashadvisor.profile.domain.impl.InputValidationInteractorImpl
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

    fun saveChanges(
        name: String,
        surname: String,
        profilePicUri: Uri?
    ) {
        viewModelScope.launch {

            val nameValidationState = inputValidationInteractor.validateName(name)
            val surnameValidationState = inputValidationInteractor.validateSurname(surname)

            if (isInputValid(nameValidationState, surnameValidationState)) {
                // Сохранить
                delay(1000)
                _sideEffects.emit(ProfileSettingsScreenSideEffects.DataSaved)
            } else {
                emitErrorMessage(nameValidationState, surnameValidationState)
            }
        }

    }

    private fun isInputValid(
        nameValidationState: InputValidationState,
        surnameValidationState: InputValidationState
    ): Boolean {
        return (nameValidationState is InputValidationState.Success &&
                surnameValidationState !is InputValidationState.Error)
    }

    private suspend fun emitErrorMessage(
        nameValidationState: InputValidationState,
        surnameValidationState: InputValidationState
    ) {
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
}