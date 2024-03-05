package app.cashadvisor.authorization.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import app.cashadvisor.authorization.presentation.viewmodel.models.EntryInteraction
import app.cashadvisor.authorization.presentation.viewmodel.models.EntryScreenState
import app.cashadvisor.common.ui.BaseViewModel
import app.cashadvisor.common.utils.debounce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EntryViewModel @Inject constructor() : BaseViewModel() {
    private var _state = MutableStateFlow<EntryScreenState>(EntryScreenState.Default)
    val state: StateFlow<EntryScreenState>
        get() = _state
    private var coolDownDebounce: ((Unit) -> Unit) = debounce(
        COOL_DOWN_DELAY,
        viewModelScope
    ) {
        _state.value = EntryScreenState.Default
    }

    fun handleInteraction(action: EntryInteraction) {
        when (action) {
            is EntryInteraction.SignUpTapped -> {
                _state.value = EntryScreenState.SignUp
            }

            is EntryInteraction.SignInTapped -> {
                _state.value = EntryScreenState.SignIn
            }
        }
        coolDownDebounce.invoke(Unit)
    }

    companion object {
        private const val COOL_DOWN_DELAY = 100L
    }
}