package app.cashadvisor.signup.presentation.ui

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import app.cashadvisor.R
import app.cashadvisor.common.ui.BaseFragment
import app.cashadvisor.databinding.FragmentSignupBinding
import app.cashadvisor.signup.presentation.viewmodel.SignupViewModel
import app.cashadvisor.signup.presentation.viewmodel.models.SignupSideEffect
import app.cashadvisor.signup.presentation.viewmodel.models.SignupUiState
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
            findNavController().navigate(R.id.action_signupFragment_to_entryVerificationFragment)
        }

        binding.edittextEnterEmail.doOnTextChanged { text, start, before, count ->
                checkEmptyInput(text, binding.edittextEnterEmail)
                viewModel.validateEmail(text.toString())
        }

        binding.edittextComeUpWithAPassword.doOnTextChanged { text, start, before, count ->
                checkEmptyInput(text, binding.edittextComeUpWithAPassword)
                viewModel.validatePassword(text.toString())
        }

        binding.edittextConfirmThePassword.doOnTextChanged { text, start, before, count ->
                checkEmptyInput(text, binding.edittextConfirmThePassword)
                viewModel.validateConfirmPassword(text.toString())
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
                    background = ResourcesCompat
                        .getDrawable(resources,
                            app.cashadvisor.uikit.R.drawable.black_button_r_10_no_stroke,
                            null)
                }

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
                viewToFocus.requestFocus()
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
        editText.background = ResourcesCompat.getDrawable(
            resources,
            app.cashadvisor.uikit.R.drawable.text_input_background_error,
            null
        )
    }

    private fun showSuccessEditText(editText: EditText) {
        editText.background = ResourcesCompat.getDrawable(
            resources,
            app.cashadvisor.uikit.R.drawable.text_input_background_succes,
            null
        )
    }

    private fun showNeutralEditText(editText: EditText) {
        editText.background = ResourcesCompat.getDrawable(
            resources,
            app.cashadvisor.uikit.R.drawable.corner_rectangle_bottomsheet,
            null
        )
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
                Toast.makeText(requireContext(), sideEffect.message, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val SNACKBAR_DURATION = 6000
    }
}