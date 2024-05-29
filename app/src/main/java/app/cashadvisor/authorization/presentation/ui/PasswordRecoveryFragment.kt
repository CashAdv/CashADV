package app.cashadvisor.authorization.presentation.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import app.cashadvisor.authorization.domain.models.states.PasswordRecoveryScreenState
import app.cashadvisor.authorization.presentation.ui.models.RecoveryScreenMessageContent
import app.cashadvisor.authorization.presentation.ui.models.RecoverySideEffect
import app.cashadvisor.authorization.presentation.viewmodel.PasswordRecoveryViewModel
import app.cashadvisor.authorization.presentation.viewmodel.models.RecoveryEmailValidationState
import app.cashadvisor.authorization.presentation.viewmodel.models.RecoveryPasswordValidationState
import app.cashadvisor.common.ui.BaseFragment
import app.cashadvisor.databinding.FragmentPasswordRecoveryBinding
import app.cashadvisor.uikit.R
import app.cashadvisor.uikit.databinding.ItemDialogNoInternetBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PasswordRecoveryFragment :
    BaseFragment<FragmentPasswordRecoveryBinding, PasswordRecoveryViewModel>(
        FragmentPasswordRecoveryBinding::inflate
    ) {
    override val viewModel: PasswordRecoveryViewModel by viewModels()
    override fun onConfigureViews() {
        with(binding) {
            btnGetCode.setOnClickListener {
                viewModel.requestRecovery()
            }
            btnSendNewPassword.setOnClickListener {
                viewModel.setPassword(etPasswordInput.text.toString())
            }
            etEmailInput.addTextChangedListener {
                viewModel.emailInputListener(it)
            }
            etConfirmationCode.setCallback {
                viewModel.setEmailConfirmCode(it)
            }
            etPasswordInput.addTextChangedListener {
                viewModel.passwordInputListener(it)
            }
            tvCantGetCode.setOnClickListener {
                //navigation to support screen. It`s not exist yet
            }
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
                viewModel.sideEffect.collect { recoverySideEffect ->
                    handleSideEffects(recoverySideEffect)

                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.messageEvent.collect { screenMessageState ->
                    showMessages(screenMessageState)

                }
            }
        }

    }

    private fun updateUi(state: PasswordRecoveryScreenState) {
        navigateBack(state)
        with(binding) {
            when (state) {
                is PasswordRecoveryScreenState.EmailInput -> {
                    customSteps.changeSteps(maxSteps = 3, currentStep = 1)
                    tvSubtitle.text = getText(R.string.enter_account_data)
                    clEmailForms.visibility = View.VISIBLE
                    clConfirmationCode.visibility = View.GONE
                    clNewPassword.visibility = View.GONE


                    btnGetCode.isEnabled = state.isBtnLoginEnabled
                    manageEmailValidation(state.emailState)

                    state.isLoginSuccessful?.let { isLoginSuccessful ->
                        if (isLoginSuccessful) {
                            hideKeyboard()
                        } else {
                            showErrorEditText(etEmailInput)
                        }
                    }
                }

                is PasswordRecoveryScreenState.ConfirmationCode -> {
                    customSteps.changeSteps(maxSteps = 3, currentStep = 2)
                    tvSubtitle.text = getText(R.string.confirmation)
                    clEmailForms.visibility = View.GONE
                    clConfirmationCode.visibility = View.VISIBLE
                    clNewPassword.visibility = View.GONE

                    if (state.resendingCoolDownSec.isNullOrBlank()) {
                        tvSendAgain.apply {
                            text = getString(R.string.send_confirmation_code_again)
                            setTextColor(resources.getColor(R.color.black, null))
                            setOnClickListener { viewModel.sendConfirmationCodeByEmail() }
                        }
                    } else {
                        tvSendAgain.apply {
                            text = getString(
                                R.string.send_confirmation_code_again_seconds,
                                state.resendingCoolDownSec
                            )
                            setTextColor(resources.getColor(R.color.subcolour2, null))
                            setOnClickListener(null)
                        }
                    }
                }

                is PasswordRecoveryScreenState.PasswordInput -> {
                    customSteps.changeSteps(maxSteps = 3, currentStep = 3)
                    tvSubtitle.text = getText(R.string.new_password)
                    clEmailForms.visibility = View.GONE
                    clConfirmationCode.visibility = View.GONE
                    clNewPassword.visibility = View.VISIBLE

                    btnSendNewPassword.isEnabled = state.isBtnResetPasswordEnabled
                    managePasswordValidation(state.passwordState)

                    state.resetPasswordSuccessful?.let { isResetPasswordSuccessful ->
                        if (isResetPasswordSuccessful) {
                            hideKeyboard()
                        } else {
                            showErrorEditText(etPasswordInput)
                        }
                    }
                }
            }


        }

    }

    private fun handleSideEffects(sideEffect: RecoverySideEffect) {
        when (sideEffect) {
            is RecoverySideEffect.PasswordSuccessfullyConfirmed -> {
                findNavController().navigate(
                    app.cashadvisor.R.id.action_passwordRecoveryFragment_to_entryVerificationFragment
                )
            }

            is RecoverySideEffect.HideKeyboard -> {
                hideKeyboard()
            }

            is RecoverySideEffect.NoInternetConnection -> {
                hideKeyboard()
                showNoInternetDialog()
            }
            is RecoverySideEffect.ClearConfirmationCode -> {
                binding.etConfirmationCode.setCode("")
            }

        }
    }

    private fun navigateBack(state: PasswordRecoveryScreenState) {
        with(binding) {
            when (state) {
                is PasswordRecoveryScreenState.ConfirmationCode -> {
                    btnBack.setOnClickListener {
                        viewModel.navigateBackToEmailState()
                    }

                    requireActivity().onBackPressedDispatcher.addCallback {
                        viewModel.navigateBackToEmailState()
                    }

                }

                is PasswordRecoveryScreenState.EmailInput -> {
                    binding.btnBack.setOnClickListener {
                        findNavController().navigateUp()
                    }

                    requireActivity().onBackPressedDispatcher.addCallback {
                        findNavController().navigateUp()
                    }
                }

                is PasswordRecoveryScreenState.PasswordInput -> {
                    binding.btnBack.setOnClickListener {
                        viewModel.navigateBackToEmailState()
                    }
                    requireActivity().onBackPressedDispatcher.addCallback {
                        viewModel.navigateBackToEmailState()
                    }
                }
            }
        }
    }

    private fun manageEmailValidation(state: RecoveryEmailValidationState?) {
        state?.let {
            with(binding) {
                when (state) {
                    is RecoveryEmailValidationState.Error -> {
                        showErrorEditText(etEmailInput)
                    }

                    is RecoveryEmailValidationState.Success -> {
                        showSuccessEditText(etEmailInput)
                    }

                    is RecoveryEmailValidationState.Default -> {
                        showNeutralEditText(etEmailInput)
                    }
                }
            }
        }

    }

    private fun managePasswordValidation(state: RecoveryPasswordValidationState?) {
        state?.let {
            with(binding) {
                when (state) {
                    is RecoveryPasswordValidationState.Error -> {
                        showErrorEditText(binding.etPasswordInput)
                        tiPasswordInput.isHelperTextEnabled = true
                        tiPasswordInput.helperText =
                            getString(R.string.password_text_input_helper_text)
                    }

                    is RecoveryPasswordValidationState.Default -> {
                        showNeutralEditText(binding.etPasswordInput)
                        tiPasswordInput.isHelperTextEnabled = false
                    }

                    is RecoveryPasswordValidationState.Success -> {
                        showSuccessEditText(binding.etPasswordInput)
                        tiPasswordInput.isHelperTextEnabled = false
                    }
                }
            }
        }
    }

    private fun showMessages(content: RecoveryScreenMessageContent) {
        with(binding) {
            when (content) {
                is RecoveryScreenMessageContent.EmailFormatError -> {
                    showSnackbar(
                        getString(R.string.wrong_email_format),
                        etEmailInput
                    )
                }

                is RecoveryScreenMessageContent.PasswordCountError -> {
                    showSnackbar(
                        getString(R.string.wrong_password_count),
                        etPasswordInput
                    )
                }

                is RecoveryScreenMessageContent.PasswordFormatError -> {
                    showSnackbar(
                        getString(R.string.wrong_password_format),
                        etPasswordInput
                    )
                }

                is RecoveryScreenMessageContent.LoginError -> {
                    showSnackbar(
                        content.message,
                        etEmailInput
                    )
                }

                is RecoveryScreenMessageContent.ConfirmationCodeMessage -> {
                    showSnackbar(
                        content.message,
                        etEmailInput
                    )
                }

                is RecoveryScreenMessageContent.ResetPasswordError -> {
                    showSnackbar(
                        content.message,
                        etEmailInput
                    )
                }
            }
        }
    }

    private fun showErrorEditText(editText: TextInputEditText) {
        editText.background = ResourcesCompat.getDrawable(
            resources,
            R.drawable.text_input_background_error,
            null
        )
    }

    private fun showSuccessEditText(editText: TextInputEditText) {
        editText.background = ResourcesCompat.getDrawable(
            resources,
            R.drawable.text_input_background_success,
            null
        )
    }

    private fun showNeutralEditText(editText: TextInputEditText) {
        editText.background = ResourcesCompat.getDrawable(
            resources,
            R.drawable.text_input_background_neutral,
            null
        )
    }

    private fun showSnackbar(message: String, viewToFocus: TextInputEditText) {
        hideKeyboard()
        Snackbar.make(binding.root, message, LoginFragment.SNACKBAR_DURATION)
            .setBackgroundTint(resources.getColor(R.color.black, null))
            .setTextColor(resources.getColor(R.color.white, null))
            .setActionTextColor(resources.getColor(R.color.white, null))
            .setAction(getString(R.string.ok)) {
                viewToFocus.requestFocus()
                showKeyboard(viewToFocus)
            }
            .show()
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun showKeyboard(view: View) {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }

    private fun showNoInternetDialog() {
        val inflater = LayoutInflater.from(requireContext())
        val dialogBinding = ItemDialogNoInternetBinding.inflate(inflater)

        val noInternetDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setBackground(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.dialog_no_internet_background,
                    null
                )
            )
            .show()

        dialogBinding.btnClose.setOnClickListener {
            noInternetDialog.dismiss()
        }
    }
}