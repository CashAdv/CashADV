package app.cashadvisor.authorization.presentation.ui

import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.cashadvisor.authorization.domain.models.states.PasswordRecoveryScreenState
import app.cashadvisor.authorization.presentation.viewmodel.PasswordRecoveryViewModel
import app.cashadvisor.authorization.presentation.viewmodel.models.RecoveryPasswordScreenEvent
import app.cashadvisor.common.ui.BaseFragment
import app.cashadvisor.databinding.FragmentPasswordRecoveryBinding
import kotlinx.coroutines.launch

class PasswordRecoveryFragment() : BaseFragment<FragmentPasswordRecoveryBinding, PasswordRecoveryViewModel>(FragmentPasswordRecoveryBinding::inflate) {
    override val viewModel: PasswordRecoveryViewModel by viewModels()
    override fun onConfigureViews() {
        with(binding){
            btnGetCode.setOnClickListener{
                viewModel.handleEvent(RecoveryPasswordScreenEvent.Recovery)
            }
            btnGetCode.setOnClickListener{
                viewModel.handleEvent(RecoveryPasswordScreenEvent.ConfirmEmail(requireContext()))
            }
            btnSendNewPassword.setOnClickListener {
                viewModel.handleEvent(RecoveryPasswordScreenEvent.ConfirmNewPassword(requireContext()))
            }
            etEmailInput.doOnTextChanged { text, _, _, _->
                viewModel.handleEvent(RecoveryPasswordScreenEvent.SetEmail(text.toString()))
            }
            etConfirmationCode.doOnTextChanged{ text, _, _, _ ->
                viewModel.handleEvent(RecoveryPasswordScreenEvent.SetEmailConfirmCode(text.toString(), requireContext()))
            }
            etPasswordInput.doOnTextChanged{text, _, _, _ ->
                viewModel.handleEvent(RecoveryPasswordScreenEvent.SetPassword(text.toString(), requireContext()))
            }
        }
    }

    override fun onSubscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{ uiState ->
                    updateUi(uiState)

                }

            }
        }
        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.sideEffect.collect{

                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.messageEvent.collect{

                }
            }
        }

    }
    private fun updateUi(state: PasswordRecoveryScreenState){
        with(binding){


        }

    }


}