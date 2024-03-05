package app.cashadvisor.authorization.presentation.ui.test

import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.cashadvisor.common.ui.BaseFragment
import app.cashadvisor.databinding.FragmentTestBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TestFragment() :
    BaseFragment<FragmentTestBinding, TestViewModel>(FragmentTestBinding::inflate) {
    override val viewModel: TestViewModel by viewModels()

    //TODO: отладочный фрагмент, перед МР все будет очищено, верстка будет приведена в первоначальное состояние

    override fun onConfigureViews() {
        binding.emailInput.doOnTextChanged { text, _, _, _ ->
            viewModel.handleEvent(TestScreenEvent.SetEmail(text.toString()))
        }

        binding.btnRegister.setOnClickListener {
            viewModel.handleEvent(TestScreenEvent.Register)
        }

        binding.emailCodeInput
            .doOnTextChanged { text, _, _, _ ->
                viewModel.handleEvent(TestScreenEvent.SetRegisterConformationCode(text.toString()))
            }

        binding.sendEmailCodeButton.setOnClickListener {
            viewModel.handleEvent(TestScreenEvent.ConfirmRegister)
        }

        binding.btnLogin.setOnClickListener {
            viewModel.handleEvent(TestScreenEvent.Login)
        }

        binding.loginCodeInput
            .doOnTextChanged { text, _, _, _ ->
                viewModel.handleEvent(TestScreenEvent.SetLoginConformationCode(text.toString()))
            }

        binding.sendLoginCodeButton.setOnClickListener {
            viewModel.handleEvent(TestScreenEvent.ConfirmLogin)
        }
    }

    override fun onSubscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    updateUi(uiState)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sideEffect.collect {
                    handleSideEffects(it)
                }
            }
        }

        viewModel.init()
    }

    private fun updateUi(uiState: TestStartUiState) {
        with(uiState) {
            binding.sendEmailCodeButton.isEnabled = registerCodeIsValid
            binding.sendLoginCodeButton.isEnabled = loginCodeIsValid
            binding.btnRegister.isEnabled = emailIsValid
            binding.btnLogin.isEnabled = emailIsValid
        }
    }

    private fun handleSideEffects(sideEffect: TestSideEffect) {
        when (sideEffect) {
            is TestSideEffect.ShowMessage ->
                Toast.makeText(requireContext(), sideEffect.message, Toast.LENGTH_LONG).show()
        }
    }

}
