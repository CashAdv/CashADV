package app.cashadvisor.profile.presentation.ui

import android.widget.EditText
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import app.cashadvisor.common.ui.BaseFragment
import app.cashadvisor.databinding.FragmentProfileSettingsBinding
import app.cashadvisor.profile.domain.api.InputValidationState
import app.cashadvisor.profile.presentation.model.ProfileSettingsScreenSideEffects
import app.cashadvisor.profile.presentation.model.ProfileSettingsScreenState
import app.cashadvisor.profile.presentation.viewmodel.ProfileSettingsViewModel
import app.cashadvisor.uikit.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileSettingsFragment :
    BaseFragment<FragmentProfileSettingsBinding, ProfileSettingsViewModel>(
        FragmentProfileSettingsBinding::inflate
    ) {

    override val viewModel: ProfileSettingsViewModel by viewModels()

    private var profilePicUrl: String? = null

    override fun onConfigureViews() {
        setChangePictureViewClickListener()
        setBtnSaveClickListener()
        setBtnBackClickListener()
        setTextWatchers()
    }

    override fun onSubscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sideEffects.collect { sideEffect ->
                    handleSideEffects(sideEffect)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    updateUi(uiState)
                }
            }
        }
    }

    private fun updateUi(uiState: ProfileSettingsScreenState) {
        when (uiState) {
            is ProfileSettingsScreenState.UserData -> {
                updateUserInfo(uiState)
            }
            is ProfileSettingsScreenState.InputValidation -> {
                updateValidationState(uiState)
            }

            else -> {}
        }
    }

    private fun updateUserInfo(uiState: ProfileSettingsScreenState.UserData) {
        setDefaultEditTexts()
        binding.etName.setText(uiState.name)
        binding.etSurname.setText(uiState.surname)
        uiState.profilePicUrl?.let {
            setImageToIV(it)
            profilePicUrl = it
        }
    }

    private fun updateValidationState(uiState: ProfileSettingsScreenState.InputValidation) {
        uiState.profilePicUrl?.let {
            if (it != profilePicUrl) {
                setImageToIV(it)
            }
        }
        binding.etName.updateState(uiState.nameInputValidationState)
        binding.etSurname.updateState(uiState.surnameInputValidationState)
    }

    private fun EditText.updateState(state: InputValidationState) {
        val backgroundResId = when (state) {
            InputValidationState.Default -> R.drawable.light_gray_text_mask
            is InputValidationState.Error -> R.drawable.text_input_background_error
            InputValidationState.Success -> R.drawable.text_input_background_success
        }
        background = ContextCompat.getDrawable(requireContext(), backgroundResId)
    }

    private fun setDefaultEditTexts() {
        binding.etName.updateState(InputValidationState.Default)
        binding.etSurname.updateState(InputValidationState.Default)
    }


    private fun handleSideEffects(sideEffect: ProfileSettingsScreenSideEffects) {
        val message = when (sideEffect) {
            ProfileSettingsScreenSideEffects.EmptyName -> "Поле \"Имя\" обязательно для заполнения"
            ProfileSettingsScreenSideEffects.IncorrectSurname -> "Фамилия может содержать от 1 до 50 латинского или русского алфавита"
            ProfileSettingsScreenSideEffects.DataSaved -> "Данные успешно обновлены"
            ProfileSettingsScreenSideEffects.FailedToSaveData -> ""
            ProfileSettingsScreenSideEffects.IncorrectName -> "Имя может содержать от 1 до 50 латинского или русского алфавита"
            ProfileSettingsScreenSideEffects.IncorrectNameAndSurname -> "Фамилия и имя могут содержать от 1 до 50 латинского или русского алфавита"
            ProfileSettingsScreenSideEffects.NoInternetConnection -> ""
        }
        showSnackbar(message)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
            .setBackgroundTint(resources.getColor(R.color.black, null))
            .setTextColor(resources.getColor(R.color.white, null))
            .setActionTextColor(resources.getColor(R.color.white, null))
            .setAction("OK") {//dismiss
            }
            .show()
    }

    private fun setChangePictureViewClickListener() {
        val pickMedia = registerPickMediaRequest()
        binding.llChangeProfilePic.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun registerPickMediaRequest() =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                profilePicUrl = uri.toString()
                setImageToIV(uri.toString())
            }
        }

    private fun setImageToIV(uri: String) {
        Glide.with(this)
            .load(uri)
            .placeholder(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.placeholder_profile_picture
                )
            )
            .apply(RequestOptions.circleCropTransform())
            .into(binding.ivProfilePic)
    }

    private fun setBtnSaveClickListener() {
        binding.btnSave.setOnClickListener {
            viewModel.saveChanges(
                name = binding.etName.text.toString(),
                surname = binding.etSurname.text.toString(),
                profilePicUrl = profilePicUrl
            )
        }
    }

    private fun setBtnBackClickListener() {
        findNavController().navigateUp()
    }

    private fun setTextWatchers() {
        binding.etName.doAfterTextChanged {
            viewModel.updateInput(name = it?.toString())
        }
        binding.etSurname.doAfterTextChanged {
            viewModel.updateInput(surname = it?.toString())
        }
    }
}