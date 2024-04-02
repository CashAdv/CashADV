package app.cashadvisor.authorization.presentation.viewmodel.passwordrecovery

import app.cashadvisor.authorization.domain.api.InputValidationInteractor
import app.cashadvisor.authorization.domain.api.LoginInteractor
import app.cashadvisor.authorization.presentation.viewmodel.passwordrecovery.models.PasswordRecoveryState
import app.cashadvisor.common.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor(
    private val loginInteractor: LoginInteractor,
    private val inputValidationInteractor: InputValidationInteractor
) : BaseViewModel() {

    private val _state: MutableStateFlow<PasswordRecoveryState> = MutableStateFlow(
        PasswordRecoveryState()
    )
    private val state: StateFlow<PasswordRecoveryState> = _state.asStateFlow()
    private val currentState get() = state.replayCache.firstOrNull() ?: PasswordRecoveryState()


    private fun setEmail(email: String) {

    }
    private fun setPassword(password :String){

    }

    private fun sendEmailConfirmCode() {

    }
    private fun setEmailConfirmCode(){

    }
}