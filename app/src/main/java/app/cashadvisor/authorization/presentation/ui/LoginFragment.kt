package app.cashadvisor.authorization.presentation.ui

import android.content.Context
import android.graphics.Typeface
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
import app.cashadvisor.authorization.domain.models.states.EmailValidationState
import app.cashadvisor.authorization.domain.models.states.PasswordValidationState
import app.cashadvisor.authorization.presentation.ui.models.LoginScreenSideEffects
import app.cashadvisor.authorization.presentation.ui.models.LoginScreenMessageContent
import app.cashadvisor.authorization.presentation.viewmodel.LoginViewModel
import app.cashadvisor.authorization.presentation.viewmodel.models.LoginScreenState
import app.cashadvisor.common.ui.BaseFragment
import app.cashadvisor.databinding.FragmentLoginBinding
import app.cashadvisor.uikit.R
import app.cashadvisor.uikit.databinding.ItemDialogNoInternetBinding
import app.cashadvisor.uikit.databinding.ItemFailedToConfirmLoginBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment :
    BaseFragment<FragmentLoginBinding, LoginViewModel>(FragmentLoginBinding::inflate) {

    override val viewModel: LoginViewModel by viewModels()

    override fun onConfigureViews() {

        with(binding){
            btnLogin.setOnClickListener {
                viewModel.validateCredentials(
                    context = requireContext(),
                    emailInput = etEmailInput.text,
                    passwordInput = etPasswordInput.text
                )
            }

            tvForgetPassword.setOnClickListener {
                findNavController().navigate(app.cashadvisor.R.id.action_loginFragment_to_passwordRecoveryFragment)
            }

            etEmailInput.addTextChangedListener {
                viewModel.credentialsInputListener(emailInput = it)
            }

            etPasswordInput.addTextChangedListener {
                viewModel.credentialsInputListener(passwordInput = it)
            }

            tvCantGetCode.setOnClickListener {
                //navigation to support screen. It`s not exist yet
            }

            etConfirmationCode.addTextChangedListener {
                it?.let { code ->
                    if (code.length == 4) {
                        viewModel.confirmLoginUsingCode(requireContext(), code)
                    }
                }
            }

            etPasswordInput.typeface = Typeface.DEFAULT_BOLD
        }
    }

    override fun onSubscribe() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginScreenState.collect { state ->
                    updateUi(state)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.messageEvent.collect { content ->
                    showMessages(content)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sideEffects.collect { sideEffect ->
                   handleSideEffects(sideEffect)
                }
            }
        }
    }

    private fun updateUi(state: LoginScreenState) {
        navigateBack(state)
        with(binding) {

            when (state) {
                is LoginScreenState.CredentialsInput -> {
                    customSteps.changeSteps(2, 1)
                    tvSubtitle.text = getString(R.string.enter_account_data)
                    clLoginForms.visibility = View.VISIBLE
                    clConfirmationCode.visibility = View.GONE
                    etConfirmationCode.text?.clear()

                    btnLogin.isEnabled = state.isBtnLoginEnabled
                    manageEmailValidation(state.emailState)
                    managePasswordValidation(state.passwordState)

                    state.isLoginSuccessful?.let { isLoginSuccessful ->
                        if (isLoginSuccessful) {
                            hideKeyboard()
                        } else {
                            showErrorEditText(etEmailInput)
                            showErrorEditText(etPasswordInput)
                        }
                    }

                    when(state.isLoading){
                        true -> {
                            btnLogin.text = ""
                            progressBar.visibility = View.VISIBLE
                            btnLogin.isClickable = false
                            hideKeyboard()
                        }
                        false -> {
                            btnLogin.text = getString(R.string.enter)
                            progressBar.visibility = View.GONE
                            btnLogin.isClickable = true
                        }
                        null -> {
                            btnLogin.text = getString(R.string.enter)
                            progressBar.visibility = View.GONE
                            btnLogin.isClickable = true
                        }
                    }
                }

                is LoginScreenState.ConfirmationCode -> {
                    customSteps.changeSteps(2, 2)
                    tvSubtitle.text = getString(R.string.confirmation)
                    clLoginForms.visibility = View.GONE
                    clConfirmationCode.visibility = View.VISIBLE
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
            }
        }
    }

    private fun manageEmailValidation(state: EmailValidationState?) {
        state?.let {
            with(binding) {
                when (state) {
                    is EmailValidationState.Error -> {
                        showErrorEditText(etEmailInput)
                    }

                    is EmailValidationState.Success -> {
                        showSuccessEditText(etEmailInput)
                    }

                    EmailValidationState.Default -> {
                        showNeutralEditText(etEmailInput)
                    }
                }
            }
        }
    }

    private fun managePasswordValidation(state: PasswordValidationState?) {
        state?.let {
            with(binding) {
                when (state) {
                    is PasswordValidationState.Error -> {
                        showErrorEditText(etPasswordInput)
                        tiPasswordInput.isHelperTextEnabled = true
                        tiPasswordInput.helperText =
                            getString(R.string.password_text_input_helper_text)
                    }

                    is PasswordValidationState.Success -> {
                        showSuccessEditText(etPasswordInput)
                        tiPasswordInput.isHelperTextEnabled = false
                    }

                    PasswordValidationState.Default -> {
                        showNeutralEditText(etPasswordInput)
                        tiPasswordInput.isHelperTextEnabled = false
                    }
                }
            }
        }
    }

    private fun showMessages(content: LoginScreenMessageContent) {
        with(binding) {
            when (content) {
                LoginScreenMessageContent.EmailAndPasswordFormatErrors -> {
                    showSnackbar(
                        getString(R.string.wrong_password_format_and_email_format),
                        etEmailInput
                    )
                }

                LoginScreenMessageContent.EmailFormatAndPasswordCountErrors -> {
                    showSnackbar(
                        getString(R.string.wrong_password_count_and_email_format),
                        etEmailInput
                    )
                }

                LoginScreenMessageContent.EmailFormatError -> {
                    showSnackbar(
                        getString(R.string.wrong_email_format),
                        etEmailInput
                    )
                }

                LoginScreenMessageContent.PasswordCountError -> {
                    showSnackbar(
                        getString(R.string.wrong_password_count),
                        etPasswordInput
                    )
                }

                LoginScreenMessageContent.PasswordFormatError -> {
                    showSnackbar(
                        getString(R.string.wrong_password_format),
                        etPasswordInput
                    )
                }

                is LoginScreenMessageContent.LoginError -> {
                    showSnackbar(
                        content.message,
                        etEmailInput
                    )
                }

                is LoginScreenMessageContent.ConfirmationCodeMessage -> {
                    showSnackbar(
                        content.message,
                        etConfirmationCode
                    )
                }
            }
        }
    }

    private fun showSnackbar(message: String, viewToFocus: TextInputEditText) {
        hideKeyboard()
        Snackbar.make(binding.root, message, SNACKBAR_DURATION)
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
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun showKeyboard(view: View) {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
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

    private fun showFailedToConfirmLoginDialog(lockDuration: String) {
        val inflater = LayoutInflater.from(requireContext())
        val dialogBinding = ItemFailedToConfirmLoginBinding.inflate(inflater)
        dialogBinding.tvTitle.text = getString(R.string.wrong_code_number_lock_duration,lockDuration )

        val noInternetDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setBackground(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.dialog_no_internet_background,
                    null
                )
            )
            .setCancelable(false)
            .show()

        dialogBinding.btnClose.setOnClickListener {
            noInternetDialog.dismiss()
            findNavController().navigate(app.cashadvisor.R.id.action_loginFragment_to_startFragment)
        }
    }

    private fun handleSideEffects(sideEffect: LoginScreenSideEffects){
        when (sideEffect) {
            LoginScreenSideEffects.NoInternetConnection -> {
                hideKeyboard()
                showNoInternetDialog()
            }

            LoginScreenSideEffects.LoginSuccessfullyConfirmed -> {
                findNavController().navigate(app.cashadvisor.R.id.action_loginFragment_to_analyticsFragment)
            }

            LoginScreenSideEffects.HideKeyboard -> {
                hideKeyboard()
            }

            is LoginScreenSideEffects.FailedToConfirmLogin -> {
                hideKeyboard()
                showFailedToConfirmLoginDialog(sideEffect.lockDuration)
            }
        }
    }

    private fun navigateBack(state: LoginScreenState){
        with(binding){
            when(state){
                is LoginScreenState.ConfirmationCode -> {
                    btnBack.setOnClickListener {
                        viewModel.navigateBackToCredentialsState()
                    }

                    requireActivity().onBackPressedDispatcher.addCallback{
                        viewModel.navigateBackToCredentialsState()
                    }

                }
                is LoginScreenState.CredentialsInput -> {
                    binding.btnBack.setOnClickListener {
                        findNavController().navigateUp()
                    }

                    requireActivity().onBackPressedDispatcher.addCallback{
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

    companion object {
        const val SNACKBAR_DURATION = 6000
    }

}