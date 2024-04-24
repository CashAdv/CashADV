package app.cashadvisor.profile.presentation.ui

import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
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
import app.cashadvisor.uikit.databinding.ItemDialogNoInternetEditingDataBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        setupEditorActionListener()
        addOnBackPressedCallback()
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
        var message: String? = null
        when (sideEffect) {
            ProfileSettingsScreenSideEffects.EmptyName -> message =
                getString(R.string.name_cant_be_empty)

            ProfileSettingsScreenSideEffects.IncorrectSurname -> message =
                getString(R.string.error_surname_format)

            ProfileSettingsScreenSideEffects.DataSaved -> message =
                getString(R.string.success_save_data)

            ProfileSettingsScreenSideEffects.FailedToSaveData -> message =
                getString(R.string.failed_to_save_data)

            ProfileSettingsScreenSideEffects.IncorrectName -> message =
                getString(R.string.error_name_format)

            ProfileSettingsScreenSideEffects.IncorrectNameAndSurname -> message =
                getString(R.string.error_name_surname_format)

            ProfileSettingsScreenSideEffects.NoInternetConnection -> showNoInternetDialog()

            ProfileSettingsScreenSideEffects.FailedToUpdateProfilePic -> message =
                getString(R.string.failed_to_update_profile_pic)

            ProfileSettingsScreenSideEffects.FailedToUpdateUsername -> message =
                getString(R.string.failed_to_update_name)

            ProfileSettingsScreenSideEffects.FailedToGetData -> message =
                getString(R.string.failed_to_get_data)

            ProfileSettingsScreenSideEffects.UndefinedError -> message =
                getString(R.string.undefined_error)
        }
        message?.let { showSnackbar(it) }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
            .setBackgroundTint(resources.getColor(R.color.black, null))
            .setTextColor(resources.getColor(R.color.white, null))
            .setActionTextColor(resources.getColor(R.color.white, null))
            .setAction(getString(R.string.ok)) {//dismiss
            }
            .show()
    }

    private fun showNoInternetDialog() {
        val inflater = LayoutInflater.from(requireContext())
        val dialogBinding = ItemDialogNoInternetEditingDataBinding.inflate(inflater)

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
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setTextWatchers() {
        binding.etName.doAfterTextChanged {
            viewModel.updateInput(name = it?.toString())
        }
        binding.etSurname.doAfterTextChanged {
            viewModel.updateInput(surname = it?.toString())
        }
    }

    private fun setupEditorActionListener() {
        binding.etName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.etName.clearFocus()
                true
            }
            false
        }
        binding.etSurname.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.etSurname.clearFocus()
                true
            }
            false
        }
    }

    private fun addOnBackPressedCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }
}