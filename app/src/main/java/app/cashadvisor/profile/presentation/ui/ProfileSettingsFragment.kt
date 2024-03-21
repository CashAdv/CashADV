package app.cashadvisor.profile.presentation.ui

import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.cashadvisor.common.ui.BaseFragment
import app.cashadvisor.databinding.FragmentProfileSettingsBinding
import app.cashadvisor.profile.presentation.viewmodel.ProfileSettingsViewModel
import app.cashadvisor.uikit.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSettingsFragment :
    BaseFragment<FragmentProfileSettingsBinding, ProfileSettingsViewModel>(
        FragmentProfileSettingsBinding::inflate
    ) {

    override val viewModel: ProfileSettingsViewModel by viewModels()

    private var profilePicUri: Uri? = null

    override fun onConfigureViews() {
        setChangePictureViewClickListener()
        setBtnSaveClickListener()
        setBtnBackClickListener()
    }

    override fun onSubscribe() {

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
                profilePicUri = uri
                setImageToIV(uri)
            }
        }

    private fun setImageToIV(uri: Uri) {
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
        viewModel.saveChanges(
            name = binding.etName.text.toString(),
            surname = binding.etSurname.text.toString(),
            profilePicUri = profilePicUri
        )
    }

    private fun setBtnBackClickListener() {
        findNavController().navigateUp()
    }
}