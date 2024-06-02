package app.cashadvisor.main.presentation.ui

import androidx.lifecycle.viewModelScope
import app.cashadvisor.authorization.data.dto.CredentialsDto
import app.cashadvisor.authorization.domain.api.AccountInformationInteractor
import app.cashadvisor.authorization.domain.api.CredentialsRepository
import app.cashadvisor.authorization.domain.models.states.AccountInformation
import app.cashadvisor.common.domain.Resource
import app.cashadvisor.common.ui.BaseViewModel
import app.cashadvisor.common.utill.extensions.logDebugMessage
import app.cashadvisor.profile.domain.api.ProfileInfoInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val interactor: AccountInformationInteractor,
    private val storage: CredentialsRepository,
    private val profileInfoInteractor: ProfileInfoInteractor
) : BaseViewModel(){

    private val _state = MutableStateFlow<AccountInformation>(AccountInformation.NotAuthorized)
    val state: StateFlow<AccountInformation>
        get() = _state

    init {
        getAccountInformation()
    }

    fun getAccountInformation() {
        viewModelScope.launch {
            interactor.getAccountInformation()
                .collect { result ->
                    Timber.tag("MainActivity").d("getAccountInformation: $result")
                    _state.value = result
                }
        }
    }
    fun getUserInfoMore(){
        viewModelScope.launch {
            when(val result = profileInfoInteractor.getUserInfoMore()) {
                is Resource.Error -> {
                    logDebugMessage(result.error.message)
                }
                is Resource.Success -> {
                    logDebugMessage(result.data.toString())
                }
            }

        }
    }
    fun getUserAnalytics(){
        viewModelScope.launch {
            when(val result = profileInfoInteractor.getUserAnalytics()){
                is Resource.Error -> {
                    logDebugMessage(result.error.message)
                }
                is Resource.Success -> {
                    logDebugMessage(result.data.toString())
                }
            }
        }
    }

    fun saveCredentials(accessToken: String, refreshToken: String) {
        viewModelScope.launch {
            storage.saveCredentials(CredentialsDto(accessToken, refreshToken))
        }
    }

    fun logout() {
        viewModelScope.launch {
            storage.clearCredentials()
        }
    }
}