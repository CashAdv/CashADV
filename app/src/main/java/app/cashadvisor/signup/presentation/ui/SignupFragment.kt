package app.cashadvisor.signup.presentation.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import app.cashadvisor.R
import app.cashadvisor.common.ui.BaseFragment
import app.cashadvisor.databinding.FragmentSignupBinding
import app.cashadvisor.signup.presentation.viewmodel.SignupViewModel
import app.cashadvisor.signup.presentation.viewmodel.models.SignUpStep
import app.cashadvisor.signup.presentation.viewmodel.models.SignupSideEffect
import app.cashadvisor.signup.presentation.viewmodel.models.SignupUiState
import app.cashadvisor.uikit.databinding.ItemDialogNoInternetBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment:
        BaseFragment<FragmentSignupBinding, SignupViewModel>(FragmentSignupBinding::inflate){

    override val viewModel: SignupViewModel by viewModels()
    override fun onConfigureViews() {

        binding.btnBack.setOnClickListener(){
            findNavController().navigateUp()
        }

        binding.btnCreateAccount.setOnClickListener(){
            viewModel.register()
        }

        binding.edittextEnterEmail.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus){
                checkEmptyInput(
                    binding.edittextEnterEmail.text,
                    binding.edittextEnterEmail
                )
                viewModel.validateEmail(binding.edittextEnterEmail.text.toString())
            }

            else showNeutralEditText(binding.edittextEnterEmail)
        }

//        binding.edittextEnterEmail.doOnTextChanged { text, start, before, count ->
//                checkEmptyInput(text, binding.edittextEnterEmail)
//                viewModel.validateEmail(text.toString())
//        }

        binding.edittextComeUpWithAPassword.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus){
                checkEmptyInput(
                    binding.edittextComeUpWithAPassword.text,
                    binding.edittextComeUpWithAPassword
                )
                viewModel.validatePassword(
                    binding.edittextComeUpWithAPassword.text.toString(),
                    binding.edittextConfirmThePassword.text.toString()
                )
            }
        }

//        binding.edittextComeUpWithAPassword.doOnTextChanged { text, start, before, count ->
//                checkEmptyInput(text, binding.edittextComeUpWithAPassword)
//                viewModel.validatePassword(
//                    text.toString(),
//                    binding.edittextConfirmThePassword.text.toString()
//                )
//        }

        binding.edittextConfirmThePassword.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus){
                checkEmptyInput(
                    binding.edittextConfirmThePassword.text,
                    binding.edittextConfirmThePassword
                )
                viewModel.validateConfirmPassword(binding.edittextConfirmThePassword.text.toString())
            }
        }

//        binding.edittextConfirmThePassword.doOnTextChanged { text, start, before, count ->
//                checkEmptyInput(text, binding.edittextConfirmThePassword)
//                viewModel.validateConfirmPassword(text.toString())
//        }

        binding.codeConfirmationView.setCallback { code ->

            if (code.length == 4) {
                viewModel.sendRegisterConfirmCode(code)
            }
        }
    }

    override fun onSubscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signupUiState.collect { signupUiState ->
                    updateUi(signupUiState)
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

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.signUpStep.collect{ signupScreenState ->
                    updateScreenState(signupScreenState)
                }
            }
        }

        viewModel.init()
    }

    private fun updateUi(signUpDataState: SignupUiState) {
        when(signUpDataState){
            is SignupUiState.EmailNotValid -> {
                showSnackbar(
                    getString(app.cashadvisor.uikit.R.string.invalid_email_format),
                    binding.edittextEnterEmail)

                showErrorEditText(binding.edittextEnterEmail)
            }

            SignupUiState.ConfirmPasswordNotValid -> {
                showSnackbar(
                    getString(app.cashadvisor.uikit.R.string.invalid_confirm_password),
                    binding.edittextConfirmThePassword)

                showErrorEditText(binding.edittextConfirmThePassword)
            }

            SignupUiState.ConfirmPasswordValid -> showSuccessEditText(binding.edittextConfirmThePassword)

            SignupUiState.EmailValid -> showSuccessEditText(binding.edittextEnterEmail)

            SignupUiState.PasswordLengthNotValid -> {
                showSnackbar(
                    getString(app.cashadvisor.uikit.R.string.invalid_password_length),
                    binding.edittextComeUpWithAPassword)

                showErrorEditText(binding.edittextComeUpWithAPassword)
            }

            SignupUiState.PasswordNotValid -> {
                showSnackbar(
                    getString(app.cashadvisor.uikit.R.string.invalid_password_format),
                    binding.edittextComeUpWithAPassword)

                showErrorEditText(binding.edittextComeUpWithAPassword)
            }

            SignupUiState.PasswordValid -> showSuccessEditText(binding.edittextComeUpWithAPassword)

            SignupUiState.SignupDataIsValid -> {
                with(binding.btnCreateAccount){
                    isEnabled = true
                    background =  ContextCompat.getDrawable(
                        requireContext(), app.cashadvisor.uikit.R.drawable.black_button_background)
                }
            }

            SignupUiState.EmailExist -> {
                showSnackbar(
                    getString(app.cashadvisor.uikit.R.string.email_already_exist),
                    binding.edittextComeUpWithAPassword)
            }
        }
    }
    private fun showSnackbar(message: String, viewToFocus: EditText
    ) {
        hideKeyboard()
        Snackbar.make(binding.root, message, SNACKBAR_DURATION)
            .setBackgroundTint(resources.getColor(app.cashadvisor.uikit.R.color.black, null))
            .setTextColor(resources.getColor(app.cashadvisor.uikit.R.color.white, null))
            .setActionTextColor(resources.getColor(app.cashadvisor.uikit.R.color.white, null))
            .setAction(getString(app.cashadvisor.uikit.R.string.ok)) {
                //viewToFocus.requestFocus()
                showKeyboard(viewToFocus)
            }
            .show()
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        inputMethodManager?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun showKeyboard(view: View){
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        inputMethodManager?.showSoftInput(view, 0)
    }

    private fun showErrorEditText(editText: EditText) {
        editText.background = ContextCompat.getDrawable(
            requireContext(), app.cashadvisor.uikit.R.drawable.text_input_background_error)
    }

    private fun showSuccessEditText(editText: EditText) {
        editText.background = ContextCompat.getDrawable(
            requireContext(), app.cashadvisor.uikit.R.drawable.text_input_background_succes)
    }

    private fun showNeutralEditText(editText: EditText) {
        editText.background = ContextCompat.getDrawable(
            requireContext(), app.cashadvisor.uikit.R.drawable.text_input_background_neutral)
    }

    private fun checkEmptyInput(text: CharSequence?, editText: EditText): Boolean{
        return if (text.isNullOrEmpty()){
            showNeutralEditText(editText)
            false
        } else true
    }

    private fun handleSideEffects(sideEffect: SignupSideEffect) {
        when (sideEffect) {
            is SignupSideEffect.ShowMessage ->
                Toast.makeText(requireContext(), getString(sideEffect.messageId), Toast.LENGTH_LONG).show()

            is SignupSideEffect.ShowChangeableMessage -> {
                Toast.makeText(
                    requireContext(),
                    getString(
                        sideEffect.messageId,
                        resources.getQuantityString(
                            sideEffect.pluralId,
                            sideEffect.messageChangeable,
                            sideEffect.messageChangeable
                        )
                    ),
                    Toast.LENGTH_LONG
                ).show()
            }

            is SignupSideEffect.NoInternetConnection -> {
                hideKeyboard()
                showNoInternetDialog()
            }
        }
    }

    private fun updateScreenState(signupScreenState: SignUpStep){
        when (signupScreenState){
            is SignUpStep.SignupScreen -> {
                with(binding){
                    customSteps.changeSteps(2, 1)
                    clSignupInputDate.visibility = View.VISIBLE
                    clSignupConfirmationCode.visibility = View.GONE
                    codeConfirmationView.setCode("")

                    edittextEnterEmail.text = null
                    edittextConfirmThePassword.text = null
                    edittextComeUpWithAPassword.text = null

                    binding.btnBack.setOnClickListener {
                        findNavController().navigateUp()
                    }

                    requireActivity().onBackPressedDispatcher.addCallback{
                        findNavController().navigateUp()
                    }
                }

            }

            is SignUpStep.ConfirmationCodeScreen -> {
                with(binding) {
                    customSteps.changeSteps(2, 2)
                    clSignupInputDate.visibility = View.GONE
                    clSignupConfirmationCode.visibility = View.VISIBLE

                    btnBack.setOnClickListener {
                        viewModel.navigateBackToCredentialsState()
                    }

                    requireActivity().onBackPressedDispatcher.addCallback{
                        viewModel.navigateBackToCredentialsState()
                    }

                    if (signupScreenState.resendingCoolDownSec.isNullOrBlank()) {
                        tvSendAgain.apply {
                            text = getString(app.cashadvisor.uikit.R.string.send_confirmation_code_again)
                            setTextColor(resources.getColor(R.color.black, null))
                            setOnClickListener { viewModel.sendConfirmationCodeByEmail() }
                        }
                    } else {
                        tvSendAgain.apply {
                            text = getString(
                                app.cashadvisor.uikit.R.string.send_confirmation_code_again_seconds,
                                signupScreenState.resendingCoolDownSec
                            )
                            setTextColor(resources.getColor(R.color.subcolor_2, null))
                            setOnClickListener(null)
                        }
                    }
                }
            }

            SignUpStep.SignupEmailSuccessfullyConfirmed -> {
                findNavController().navigate(R.id.action_signupFragment_to_analyticsFragment)
            }
        }
    }

    private fun showNoInternetDialog() {
        val inflater = LayoutInflater.from(requireContext())
        val dialogBinding = ItemDialogNoInternetBinding.inflate(inflater)

        val noInternetDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setBackground(
                ContextCompat.getDrawable(
                    requireContext(), app.cashadvisor.uikit.R.drawable.dialog_no_internet_background)
            )
            .show()

        dialogBinding.btnClose.setOnClickListener {
            noInternetDialog.dismiss()
        }
    }

    companion object {
        const val SNACKBAR_DURATION = 6000
    }
}